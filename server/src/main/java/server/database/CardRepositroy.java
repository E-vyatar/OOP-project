package server.database;

import commons.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CardRepositroy extends CrudRepository<Card, Long> {

    /**
     * Find a card given a cardId
     * @param cardId the card's id
     * @return an optional filled with the card if the card exists, otherwise an empty optional
     */
    Optional<Card> findById(long cardId);

    /**
     * Get all cards within a certain list
     * It should be returned in order of index
     * (TODO: actually write it so that it returns them in order)
     * @param listId the id of the list
     * @return an iterator of the cards.
     */
    Iterable<Card> findAllByListIdOrderByIdxAsc(long listId);

    /**
     * Find all cards in a certain board
     * @param boardId the board's id
     * @return an iterator over all found cards
     */
    Iterable<Card> findAllByBoardId(long boardId);
}
