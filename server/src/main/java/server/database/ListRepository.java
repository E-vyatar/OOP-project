package server.database;

import commons.CardList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListRepository extends CrudRepository<CardList, Long> {

    /**
     * Get all CardLists in a certain board
     *
     * @param boardId the id of the board
     * @return an iterator that iterates over all the CardLists
     */
    Iterable<CardList> findAllByBoardId(long boardId);

    /**
     * Find a specific CardList by id
     *
     * @param listId the list's id
     * @return an optional of the cardlist if it found one, otherwise an empty optional
     */
    Optional<CardList> findById(long listId);

    /**
     * Count the cards in a list
     * @param boardId The id of the list
     * @return Number of cards in a list
     */
    long countByBoardId(long boardId);

    /**
     * Update the index of all lists in a board
     *
     * @param boardId the id of the board where the list is removed from
     * @param idx    the index of the list
     */
    @Modifying
    @Query(value = "UPDATE CardList c SET c.idx = c.idx - 1 WHERE c.boardId = ?1 AND c.idx > ?2")
    void moveAllCardsHigherThanIndexDown(long boardId, long idx);
}
