package server.database;

import commons.Board;
import org.springframework.data.repository.CrudRepository;

@SuppressWarnings("unused")
public interface BoardRepository extends CrudRepository<Board, Long> {
}