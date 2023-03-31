package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CardRepositroy extends CrudRepository<Card, Long> {

    /**
     * Find a card given a cardId
     *
     * @param cardId the card's id
     * @return an optional filled with the card if the card exists, otherwise an empty optional
     */
    Optional<Card> findById(long cardId);

    /**
     * Get all cards within a certain list
     * It should be returned in order of index
     * (TODO: actually write it so that it returns them in order)
     *
     * @param listId the id of the list
     * @return an iterator of the cards.
     */
    Iterable<Card> findAllByListIdOrderByIdxAsc(long listId);

    /**
     * Find all cards in a certain board
     *
     * @param boardId the board's id
     * @return an iterator over all found cards
     */
    Iterable<Card> findAllByBoardId(long boardId);

    /**
     * Count the cards in a list
     * @param listId The id of the list
     * @return Number of cards in a list
     */
    long countByListId(long listId);

    /**
     * Update the index of all cards in a list
     *
     * @param listId   the id of the list
     * @param idx      the index of the card
     * @param newIndex the new index of the card
     */
    @Modifying
    @Query(value =
        "UPDATE Card c SET c.idx = c.idx - 1 WHERE c.listId = ?1 AND c.idx > ?2 AND c.idx <= ?3")
    void updateIdxBetweenDown(long listId, long idx, long newIndex);

    /**
     * Update the index of all cards in a list
     *
     * @param listId   the id of the list
     * @param idx      the index of the card
     * @param newIndex the new index of the card
     */
    @Modifying
    @Query(value =
        "UPDATE Card c SET c.idx = c.idx + 1 WHERE c.listId = ?1 AND c.idx >= ?2 AND c.idx < ?3")
    void updateIdxBetweenUp(long listId, long newIndex, long idx);

    /**
     * Update the index of all cards in a list
     *
     * @param listId the id of the list where the card is removed from
     * @param idx    the index of the card
     */
    @Modifying
    @Query(value = "UPDATE Card c SET c.idx = c.idx - 1 WHERE c.listId = ?1 AND c.idx > ?2")
    void moveAllCardsHigherThanIndexDown(long listId, long idx);

    /**
     * Update the index of all cards in a list
     *
     * @param newListId the id of the list (where a card is moved to)
     * @param newIndex  the index of where a card is moved to
     */
    @Modifying
    @Query(value = "UPDATE Card c SET c.idx = c.idx + 1 WHERE c.listId = ?1 AND c.idx >= ?2")
    void moveAllCardsHigherEqualThanIndexUp(long newListId, long newIndex);
}
