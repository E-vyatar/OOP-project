package server.api;

import commons.Board;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.Optional;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("all")
    public Iterable<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @GetMapping("{id}")
    public Optional<Board> getBoardById(@PathVariable("id") long id) {
        return boardRepository.findById(id);
    }

    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public Board updateBoard(@PathVariable("id") long id, @RequestBody Board board) {
        if (boardRepository.findById(id).isPresent()) {
            board.setId(id);
            return boardRepository.save(board);
        }
        return null;
    }
}
