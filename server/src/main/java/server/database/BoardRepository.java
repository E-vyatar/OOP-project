package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Get a list of boards by their ids.
     * The board's only contain the id and title, not the CardLists.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return the list of boards
     */
    @Override
    List<Board> findAllById(Iterable<Long> ids);
}