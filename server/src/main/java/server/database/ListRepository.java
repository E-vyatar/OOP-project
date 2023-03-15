package server.database;

import commons.CardList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListRepository extends CrudRepository<CardList, Long> {
    Iterable<CardList> findAllByBoardId(long boardId);
    Optional<CardList> findById(long listId);
}
