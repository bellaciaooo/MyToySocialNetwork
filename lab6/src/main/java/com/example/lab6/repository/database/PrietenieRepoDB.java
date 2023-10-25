package com.example.lab6.repository.database;

import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.validators.Validator;
import com.example.lab6.repository.database.AbstractDBRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public class PrietenieRepoDB extends AbstractDBRepository<Long, Prietenie> {

    public PrietenieRepoDB(String url, String username, String password, Validator<Prietenie> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Prietenie findOne(Long id) {
        Prietenie prietenie = null;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {   PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from friendships WHERE id = (?)");
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
                return null;

            Long idPrietenie = resultSet.getLong("id");
            Long id1 = resultSet.getLong("idu1");
            Long id2 = resultSet.getLong("idu2");
            LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();
            String status = resultSet.getString("status");

            prietenie = new Prietenie(id1,id2,friendsFrom,status);
            prietenie.setId(id);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("idu1");
                Long id2 = resultSet.getLong("idu2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();
                String status = resultSet.getString("status");

                Prietenie prietenie = new Prietenie(id1,id2,friendsFrom,status);
                prietenie.setId(id);
                friendships.add(prietenie);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Prietenie save(Prietenie prietenie) {
        String sql = "INSERT INTO friendships(id,idu1,idu2,friends_from,status) VALUES(?,?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(url,username,password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,prietenie.getId());
            preparedStatement.setLong(2,prietenie.getId1());
            preparedStatement.setLong(3,prietenie.getId2());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(prietenie.getFriendsFrom()));
            preparedStatement.setString(5,prietenie.getStatus());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie delete(Long id) {
        Prietenie prietenie= this.findOne(id);
        if(prietenie == null)
            return null;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM friendships WHERE id = (?)");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public Prietenie update(Prietenie prietenie) {
        Prietenie friendship = this.findOne(prietenie.getId());
        if(friendship == null)
            return prietenie;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE friendships SET friends_from = (?), status = (?) WHERE id = (?)");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(prietenie.getFriendsFrom()));
            preparedStatement.setString(2, prietenie.getStatus());
            preparedStatement.setLong(3,prietenie.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
