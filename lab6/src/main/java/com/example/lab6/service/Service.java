package com.example.lab6.service;

import com.example.lab6.domain.Message;
import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.UtilizatorPrietenieDTO;
import com.example.lab6.domain.validators.ValidationException;
import com.example.lab6.repository.Repository0;
import com.example.lab6.utils.events.ChangeEventType;
import com.example.lab6.utils.events.FriendshipEntityChangeEvent;
import com.example.lab6.utils.events.UserEntityChangeEvent;
import com.example.lab6.utils.observer.Observable;
import com.example.lab6.utils.observer.Observer;

import java.io.ObjectStreamClass;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

public class Service implements Observable<FriendshipEntityChangeEvent> {
    private Repository0<Long, Utilizator> repo;
    private Repository0<Long, Prietenie> repoPrietenie;
    private Repository0<Long, Message> repoMessages;

    private List<Observer<FriendshipEntityChangeEvent>> observers = new ArrayList<>();

    //pt repo fara clasa Prietenie
    public Service(Repository0<Long, Utilizator> repo) {
        this.repo = repo;
    }

    //pt repo ce implica clasa Prietenie
    public Service(Repository0<Long, Utilizator> repo, Repository0<Long, Prietenie> repoPrietenie) {
        this.repo = repo;
        this.repoPrietenie = repoPrietenie;
    }

    //pt repo cu mesaje
    public Service(Repository0<Long, Utilizator> repo, Repository0<Long, Prietenie> repoPrietenie, Repository0<Long, Message> repoMessages) {
        this.repo = repo;
        this.repoPrietenie = repoPrietenie;
        this.repoMessages = repoMessages;
    }

    //observer functions
    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipEntityChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }

    //cauta utilizatorul dupa emal
    public Utilizator findOneEmail(String email){
        for(Utilizator utilizator: this.repo.findAll())
           if(Objects.equals(utilizator.getEmail(), email))
               return utilizator;

        return null;
    }

   /* public void addUser(Long id,String firstName, String lastName, String email, String password) {
        if(this.repo.findOne(id) != null)
            throw new IllegalArgumentException("Exista deja un utilizator cu id-ul introdus.");
        Utilizator u = new Utilizator(firstName,lastName,email,password);
        u.setId(id);
        this.repo.save(u);
    }*/

    public void saveUser(String firstName, String lastName, String email, String password) {
        if(this.findOneEmail(email) != null)
            throw new IllegalArgumentException("Exista deja un utilizator cu email-ul introdus.");
        Utilizator u = new Utilizator(firstName,lastName,email,password);
        this.repo.save(u);
    }

    public void saveMessage(Long idSender, Long idReceiver, String content) {
        Message message = new Message(idSender,idReceiver,content,LocalDateTime.now());
        this.repoMessages.save(message);
        this.notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.ADD,message));
    }

    public void removeUser(Long idUser) {
        Utilizator user = this.repo.findOne(idUser);
        if(user == null)
            throw new IllegalArgumentException("Utilizatorul cu id-ul introdus nu exista.");
        //stergem lista de prieteni a userului
        for(Prietenie prietenie: this.repoPrietenie.findAll())
            if(Objects.equals(prietenie.getId1(), user.getId()) || Objects.equals(prietenie.getId2(), user.getId()))
                this.repoPrietenie.delete(prietenie.getId());
        //stergem userul
        this.repo.delete(idUser);
    }

    public Iterable<Utilizator> users() {
        return this.repo.findAll();
    }

    public Iterable<Prietenie> prietenii() {
        return this.repoPrietenie.findAll();
    }

    private boolean areFriends(Utilizator u1, Utilizator u2) {
        for (Prietenie prietenie: this.repoPrietenie.findAll()) {
            if (Objects.equals(prietenie.getId1(), u1.getId()) && Objects.equals(prietenie.getId2(), u2.getId()) && Objects.equals(prietenie.getStatus(), "accepted"))
                return true;
            else if (Objects.equals(prietenie.getId1(), u2.getId()) && Objects.equals(prietenie.getId2(), u1.getId()) && Objects.equals(prietenie.getStatus(), "accepted"))
                return true;
        }
        return false;
    }

    public void addFriendship(Long id1, Long id2) {
        Utilizator u1 = this.repo.findOne(id1);
        Utilizator u2 = this.repo.findOne(id2);

        if(u1 == null || u2 == null)
            throw new IllegalArgumentException("Unul dintre utilizatori nu exista");
        if (Objects.equals(u1.getId(), u2.getId()))
            throw new ValidationException("Primul si al doilea utilizator sunt acelasi!");
        if (this.areFriends(u1, u2)) {
            throw new ValidationException("Utilizatorul " + u2.getId() + " este deja prieten cu utilizatorul " + u1.getId());
        }
        Prietenie prietenie = new Prietenie(id1, id2, LocalDateTime.now(),"pending");
        Long ID = Long.min(id1, id2) * 1000 + Long.max(id1,id2);
        prietenie.setId(ID);

        this.repoPrietenie.save(prietenie);
        this.notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.ADD,prietenie));
    }

    public void removeFriendship(Long id1, Long id2){
        Utilizator u1 = this.repo.findOne(id1);
        Utilizator u2 = this.repo.findOne(id2);

        if(u1 == null || u2 == null)
            throw new IllegalArgumentException("Unul dintre utilizatori nu exista");
        /*if(!this.areFriends(u1,u2)){
            throw new ValidationException("Utilizatorul "+u1.getId() +" si utilizatorul "+ u2.getId() + " nu sunt prieteni ");
        }*/

        Long ID = Long.min(id1, id2) * 1000 + Long.max(id1, id2);

        this.repoPrietenie.delete(ID);
        this.notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.DELETE,this.findFriendship(id1,id2)));
    }

    public List<Utilizator> friendList(Long idUser){
        List<Utilizator> friends = new ArrayList<>();
        for(Prietenie prietenie: this.repoPrietenie.findAll()) {
            if (Objects.equals(prietenie.getId1(), idUser) && Objects.equals(prietenie.getStatus(), "accepted"))
                friends.add(this.repo.findOne(prietenie.getId2()));
            if (Objects.equals(prietenie.getId2(), idUser) && Objects.equals(prietenie.getStatus(), "accepted"))
                friends.add(this.repo.findOne(prietenie.getId1()));
        }
        return friends;
    }

    private void dfs(Utilizator utilizator, Map<Long, Boolean> visited, List<Utilizator> comp) {
        visited.put(utilizator.getId(),true);
        comp.add(utilizator);
        for (Utilizator friend : friendList(utilizator.getId())) {
            if(visited.get(friend.getId()) == null) {
                dfs(friend, visited, comp);
            }
        }
    }

    public int getComunitati(List<List<Utilizator>> listComunitati) {
        int comunitati = 0;
        Map<Long, Boolean> visited = new HashMap<Long, Boolean>();
        for (Utilizator utilizator : this.repo.findAll()) {
            if(visited.get(utilizator.getId()) == null){
                List<Utilizator> auxList = new ArrayList<Utilizator>();
                dfs(utilizator, visited, auxList);
                listComunitati.add(auxList);
                ++comunitati;
            }
        }
        return comunitati;
    }

    private int bfs(List<Utilizator> users, Utilizator start) {
        Map<Long, Integer> distances = new HashMap<>();

        distances.put(start.getId(), 0);
        Queue<Utilizator> utilizatorQueue = new LinkedList<>();
        utilizatorQueue.add(start);

        while (!utilizatorQueue.isEmpty()) {
            Utilizator utilizator = utilizatorQueue.remove();
            Integer dist = distances.get(utilizator.getId());
            for (Utilizator friend : friendList(utilizator.getId())) {
                if (distances.get(friend.getId()) == null) {
                    distances.put(friend.getId(), 1 + dist);
                    utilizatorQueue.add(friend);
                } else if (distances.get(friend.getId()) > 1 + dist) {
                    distances.replace(friend.getId(), 1 + dist);
                    utilizatorQueue.add(friend);
                }
            }
        }

        int ans = 0;
        for (Map.Entry<Long, Integer> entries : distances.entrySet()) {
            ans = Integer.max(ans, entries.getValue());
        }

        return ans;
    }

    public List<Utilizator> getComunitateSociabila(List<List<Utilizator>> listComunitati) {
        int ansVal = 0;
        List<Utilizator> comunitateSociabila = new ArrayList<>();
        for (List<Utilizator> componenta : listComunitati) {
            int ansInt = 0;
            for (Utilizator utilizator : componenta) {
                ansInt = Integer.max(ansInt, bfs(componenta,utilizator));
            }

            if (ansInt > ansVal) {
                ansVal = ansInt;
                comunitateSociabila = componenta;
            }
        }
        return comunitateSociabila;
    }

    public void updateUser(Long id, String firstName, String lastName) {
        if(this.repo.findOne(id) == null)
            throw new IllegalArgumentException("Utilizatorul cu id-ul introdus nu exista.");
        Utilizator newU = new Utilizator(firstName,lastName);
        newU.setId(id);

        this.repo.update(newU);
    }

    public void updateFriendship(Long id1, Long id2, LocalDateTime friendsFrom, String status) {
        Utilizator u1 = this.repo.findOne(id1);
        Utilizator u2 = this.repo.findOne(id2);

        if(u1 == null || u2 == null)
            throw new IllegalArgumentException("Unul dintre utilizatori nu exista");
        if (Objects.equals(u1.getId(), u2.getId()))
            throw new ValidationException("Primul si al doilea utilizator sunt acelasi!");
        /*if(!this.areFriends(u1,u2))
            throw new ValidationException("Utilizatorul "+u1.getId() +" si utilizatorul "+ u2.getId() + " nu sunt prieteni ");
        */

        Long idPrietenie = Long.min(id1, id2) * 1000 + Long.max(id1, id2);
        Prietenie newPrietenie = new Prietenie(id1,id2,friendsFrom,status);
        newPrietenie.setId(idPrietenie);
        this.repoPrietenie.update(newPrietenie);
        this.notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.UPDATE,newPrietenie));
    }

    public Utilizator loginUser(String email,String password){
        Utilizator u = this.findOneEmail(email);
        if(u == null)
            throw new IllegalArgumentException("Nu exista utilizator cu emailul introdus");
        if(!Objects.equals(u.getPassword(), password))
            throw new ValidationException("Parola introdusa nu este corecta");

        return u;
    }

    public List<UtilizatorPrietenieDTO> getFriendshipsRequests(Long idUser){
        List<UtilizatorPrietenieDTO> friendshipsUser = new ArrayList<>();
        for(Prietenie prietenie: this.repoPrietenie.findAll()) {
            if (Objects.equals(prietenie.getId2(), idUser) && (Objects.equals(prietenie.getStatus(), "accepted") || Objects.equals(prietenie.getStatus(), "pending"))){
                Utilizator userFriend = this.repo.findOne(prietenie.getId1());
                UtilizatorPrietenieDTO request = new UtilizatorPrietenieDTO(userFriend.getId(),userFriend.getFirstName(),
                        userFriend.getLastName(),prietenie.getFriendsFrom(),prietenie.getStatus());
                friendshipsUser.add(request);
            }

        }
        return friendshipsUser;
    }

    public List<Utilizator> getNotFriends(Long idUser){
        List<Utilizator> notFriends = new ArrayList<>();
        List<Utilizator> friends = friendList(idUser);

        for(Utilizator utilizator: this.repo.findAll())
            if(!friends.contains(utilizator) && !Objects.equals(utilizator.getId(), idUser))
                notFriends.add(utilizator);

        return notFriends;
    }

    public Prietenie findFriendship(Long id1,Long id2){
        Long idPrietenie = Long.min(id1, id2) * 1000 + Long.max(id1, id2);
        return repoPrietenie.findOne(idPrietenie);
    }

    public List<UtilizatorPrietenieDTO> getSentRequests(Long idUser){
        List<UtilizatorPrietenieDTO> requestsUser = new ArrayList<>();
        for(Prietenie prietenie: this.repoPrietenie.findAll()) {
            if (Objects.equals(prietenie.getId1(), idUser) && Objects.equals(prietenie.getStatus(), "pending")){
                Utilizator userRequested = this.repo.findOne(prietenie.getId2());
                UtilizatorPrietenieDTO request = new UtilizatorPrietenieDTO(userRequested.getId(),userRequested.getFirstName(),
                        userRequested.getLastName(),prietenie.getFriendsFrom(),prietenie.getStatus());
                requestsUser.add(request);
            }

        }
        return requestsUser;
    }

    public List<Message> getMyMessagesWithUser(Long myId, Long userId){
        List<Message> myMessages = new ArrayList<>();
        for(Message message: this.repoMessages.findAll()){
            if((Objects.equals(message.getSenderId(), myId) && Objects.equals(message.getReceiverId(), userId))
                    || (Objects.equals(message.getSenderId(), userId) && Objects.equals(message.getReceiverId(), myId)))
                myMessages.add(message);
        }
        myMessages.sort(Comparator.comparing(Message::getSendingDate));
        return myMessages;
    }

}

