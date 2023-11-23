package com.example.backend.repositories;

import com.example.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findAllBySenderIdAndRecipientIdOrderByCreateAt(String senderId, String recipientId);

// #TODO объеденить запросы в 1
    @Query("select distinct m.recipientId\n" +
            "from Message m\n" +
            "where m.senderId = :id\n" +
            "  and m.recipientId IS NOT NULL")
    Set<String> findAllUsersIdWhoDoWeHaveCorrespondenceWith(String id);


    @Query("select distinct m.senderId\n" +
            "from Message m\n" +
            "where m.recipientId = :id\n" +
            "  and m.senderId IS NOT NULL")
    Set<String> findAllUsersIdWhoDoWriteUs(String id);

}
