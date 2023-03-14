package server.database;

import commons.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CardRepositroy extends CrudRepository<Card, Long> {
    Optional<Card> findById(long cardId);

    Iterable<Card> findAllByListId(long listId);

    Iterable<Card> findAllByBoardId(long boardId);
}