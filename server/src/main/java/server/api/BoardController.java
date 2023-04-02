package server.api;

import commons.Board;
import commons.CardList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.function.ServerResponse;
import server.database.BoardRepository;
import server.database.ListRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;
    private Logger logger = LoggerFactory.getLogger(BoardController.class);

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
     * @param updatedBoard the board to be updated - should not include CardLists
     * @return the updated board - the list of CardList is not included (unless included in request)
     */
    @PostMapping(value = "update", consumes = "application/json", produces = "application/json")
    public ServerResponse updateBoard(@RequestBody Board updatedBoard) {
        logger.info("updateBoard() called with board: " + updatedBoard);

        var optBoard = boardRepository.findById(updatedBoard.getId());
        if (optBoard.isPresent()) {
            // We're changing the old board instead of the new one.
            // We do this because otherwise the persistence framework will assign a new id
            Board board = optBoard.get();
            board.setTitle(updatedBoard.getTitle());
            boardRepository.save(board);

            for (Consumer<Board> listener : listeners.values()) {
                listener.accept(updatedBoard);
            }
            return ServerResponse.ok().build();
        } else {
            return ServerResponse.notFound().build();
        }
    }

    // We use a Concurrent HashMap to prevent race conditions.
    private Map<Object, Consumer<Board>> listeners = new ConcurrentHashMap<>();

    /**
     * Long poll for updates to a card.
     *
     * @param boardId the board for which to poll for updates
     *
     * @return the deferred result. This will wait for 5s
     *      to return a card and otherwise not return anything.
     */
    @GetMapping(value = "updates/{id}", produces = "application/json")
    public DeferredResult<Board> longPollForUpdates(@PathVariable("id") long boardId) {

        var timeout = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        DeferredResult<Board> defResult = new DeferredResult<>(5000L, timeout);

        Object key = new Object();
        listeners.put(key, board -> {
            if (board.getId() == boardId) {
                defResult.setResult(board);
            }
        });

        // Make sure we always clean up.
        defResult.onCompletion(() -> {
            listeners.remove(key);
        });

        return defResult;
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

    /**
     * Find all boards with the id given by the client
     *
     * @param ids the boards to find in the DB
     * @return the boards
     */
    @PostMapping("find")
    public Iterable<Board> findBoards(@RequestBody List<Long> ids) {
        return boardRepository.findAllById(ids);
    }
}
