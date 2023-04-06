package server.api;

import commons.Board;
import commons.CardList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.ListRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;
    private final Logger logger = LoggerFactory.getLogger(CardController.class);

    /**
     * Constructor
     *
     * @param boardRepository the repository (used for all board-related queries)
     * @param listRepository the list repository (used to initialize a new board with empty lists)
     */
    public BoardController(BoardRepository boardRepository, ListRepository listRepository) {
        this.boardRepository = boardRepository;
        this.listRepository = listRepository;
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
     * @param id    the id of the board
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

    /** sends message back to user that board has been saved
     * @param board board
     * @return board after saved to db, containing 3 lists
     */
    @MessageMapping("/boards/new")
    @SendTo("/topic/boards/new")
    public Board addMessage(Board board){
        logger.info("addMessage() called with : board = [" + board.toString() + "]");
        Board sent = boardRepository.save(board);
        List<CardList> cardLists = List.of(
                new CardList("To Do", board.getId(), 0),
                new CardList("Doing", board.getId(), 1),
                new CardList("Done", board.getId(), 2)
        );
        sent.getCardLists().addAll(cardLists);
        listRepository.saveAll(cardLists);
        return sent;
    }
    /**
     * Create a new board
     *
     * @param board the board to create
     * @return the created board
     */
    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public Board createBoard(@RequestBody Board board) {
        logger.info("createBoard() called with: board = [" + board + "]");
        // We add three empty lists, so that the user can make a quick start
        Board result = boardRepository.save(board);
        List<CardList> cardLists = List.of(
                new CardList("To Do", board.getId(), 0),
                new CardList("Doing", board.getId(), 1),
                new CardList("Done", board.getId(), 2)
        );
        result.getCardLists().addAll(cardLists);
        listRepository.saveAll(cardLists);
        return result;
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

    @MessageMapping("/boards/delete") // app/boards/delete
    @SendTo("/topic/boards/delete")
    public Long deleteMessage(Long id){
        boardRepository.deleteById(id);
        logger.info("board has been deleted from db");
        return id;
    }
}
