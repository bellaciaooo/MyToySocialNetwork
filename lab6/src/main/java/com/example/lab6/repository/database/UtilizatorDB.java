package com.example.lab6.repository.database;

import com.example.lab6.domain.Utilizator;
import com.example.lab6.domain.validators.Validator;
import com.example.lab6.repository.database.AbstractDBRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UtilizatorDB extends AbstractDBRepository<Long, Utilizator> {

    public UtilizatorDB(String url, String username, String password, Validator<Utilizator> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Utilizator findOne(Long id) {
        Utilizator user = null;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {   PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE id = (?)");
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next())
                return null;

            Long idUser = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            user = new Utilizator(firstName, lastName, email, password);
            user.setId(idUser);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e){}
        return user;
    }

    /*//returneaza utilizatorul in functie de email
    public Utilizator findOneEmail(String email) {
        Utilizator user = null;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {   PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE email = (?)");
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next())
                return null;

            Long idUser = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String emailUser = resultSet.getString("email");
            String password = resultSet.getString("password");

            user = new Utilizator(firstName, lastName, emailUser, password);
            user.setId(idUser);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e){}
        return user;
    }
*/
    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(firstName, lastName, email, password);
                utilizator.setId(id);
                users.add(utilizator);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator utilizator) {
        String sql = "INSERT INTO users(first_name,last_name,email,password) VALUES(?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(url,username,password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,utilizator.getFirstName());
            preparedStatement.setString(2,utilizator.getLastName());
            preparedStatement.setString(3,utilizator.getEmail());
            preparedStatement.setString(4,utilizator.getPassword());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long id) {
        Utilizator user = this.findOne(id);
        if(user == null)
            return null;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = (?)");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Utilizator update(Utilizator utilizator) {
        Utilizator user = this.findOne(utilizator.getId());
        if(user == null)
            return utilizator;

        try(Connection connection= DriverManager.getConnection(url,username,password))
        {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET first_name = (?), last_name = (?) WHERE id = (?)");
            preparedStatement.setString(1, utilizator.getFirstName());
            preparedStatement.setString(2, utilizator.getLastName());
            preparedStatement.setLong(3, utilizator.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
