package server.api;

import commons.Board;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.Optional;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardRepository boardRepository;

    /**
     * Constructor
     *
     * @param boardRepository the repository (has all the necessary queries)
     */
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Get all boards
     *
     * @return all boards
     */

    @GetMapping("all")
    public Iterable<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * Get a board by id
     *
     * @param id the id of the board
     * @return the board
     */
    @GetMapping("{id}")
    public Optional<Board> getBoardById(@PathVariable("id") long id) {
        return boardRepository.findById(id);
    }

    /**
     * Updates a board
     *
     * @param id the id of the board
     * @param board the board to be updated
     * @return the updated board
     */
    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public Board updateBoard(@PathVariable("id") long id, @RequestBody Board board) {
        if (boardRepository.findById(id).isPresent()) {
            board.setId(id);
            return boardRepository.save(board);
        }
        return null;
    }

    /**
     * Create a new board
     *
     * @param board the board to create
     * @return the created board
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }

    /**
     * deletes board by id
     *
     * @param id the id of the board
     */
    @DeleteMapping("{id}")
    public void deleteBoard(@PathVariable("id") long id) {
        boardRepository.deleteById(id);
    }
}
