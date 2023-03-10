package server.database;

import commons.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@SuppressWarnings("SpringDataMongoDBJsonFieldInspection")
public interface BoardRepository extends MongoRepository<Board, String> {
    @Query("{_id:  '?0'}")
    Board findBoardById(String id);

    @Query("{boardId:  '?0'}")
    List<Board> findBoardsByBoardId(String boardId);

    long count();
}