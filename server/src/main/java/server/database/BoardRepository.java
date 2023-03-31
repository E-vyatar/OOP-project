package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("unused")
public interface BoardRepository extends JpaRepository<Board, Long> {
}