package server.database;

import commons.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@SuppressWarnings("SpringDataMongoDBJsonFieldInspection")
public interface CardRepositroy extends MongoRepository<Card, String> {
    @Query("{_id:  '?0'}")
    Card findCardById(String id);

    @Query("{boardId:  '?0'}")
    List<Card> findCardsByBoardId(String boardId);

    long count();
}