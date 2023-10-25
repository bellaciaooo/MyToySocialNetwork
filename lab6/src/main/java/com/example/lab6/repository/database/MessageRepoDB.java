package com.example.lab6.repository.database;

import com.example.lab6.domain.Message;
import com.example.lab6.domain.Prietenie;
import com.example.lab6.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MessageRepoDB extends AbstractDBRepository<Long, Message>{

    public MessageRepoDB(String url, String username, String password, Validator<Message> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Message findOne(Long aLong) {
        return null;
    }

    public Message findMessage(Long idSender, Long idReceiver) {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages");
            ResultSet resultSet = statement.executeQuery())
        {
            while(resultSet.next()) {
                Long idSender = resultSet.getLong("id_sender");
                Long idReceiver = resultSet.getLong("id_receiver");
                String content = resultSet.getString("content");
                LocalDateTime sendingDate = resultSet.getTimestamp("sending_date").toLocalDateTime();

                Message message = new Message(idSender, idReceiver, content, sendingDate);
                messages.add(message);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message message) {
        String sql = "INSERT INTO messages(id_sender,id_receiver,content,sending_date) VALUES(?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(url,username,password)){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,message.getSenderId());
            preparedStatement.setLong(2,message.getReceiverId());
            preparedStatement.setString(3,message.getContent());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(message.getSendingDate()));
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
