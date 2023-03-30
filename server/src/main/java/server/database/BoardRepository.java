package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

@SuppressWarnings("unused")
public interface BoardRepository extends JpaRepository<Board, Long> {
}