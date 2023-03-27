package server.database;

import commons.CardList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListRepository extends CrudRepository<CardList, Long> {

    /**
     * Get all CardLists in a certain board
     * @param boardId the id of the board
     * @return an iterator that iterates over all the CardLists
     */
    Iterable<CardList> findAllByBoardId(long boardId);

    /**
     * Find a specific CardList by id
     * @param listId the list's id
     * @return an optional of the cardlist if it found one, otherwise an empty optional
     */
    Optional<CardList> findById(long listId);
}
