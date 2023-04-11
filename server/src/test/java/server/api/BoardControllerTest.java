package server.api;

import commons.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.AdminService;
import server.database.BoardRepository;
import java.util.Optional;
import static org.mockito.Mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isNotNull;

public class BoardControllerTest {

    @Mock
    private AdminService adminService;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private SimpMessagingTemplate msgs;
    @InjectMocks
    private BoardController boardController;

    private MockitoSession mockito;


    @BeforeEach
    public void setup() {
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
   }

    @Test
    public void testGetAllBoardsUnauthorized() {
        when(adminService.isValidPassword(isNotNull())).thenReturn(false);
        var response = boardController.getAllBoards("password");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "You shouldn't be allowed to get all boards without proper authentication");
        mockito.finishMocking();
    }
    @Test
    public void testgetAllBoards() {
        when(adminService.isValidPassword(isNotNull())).thenReturn(true);
        Board board1 = new Board(2L, "Board 2");
        Board board2 = new Board(5L, "Board 5");
        List<Board> boards = List.of(board1, board2);
        when(boardRepository.findAll()).thenReturn(boards);

        var response = boardController.getAllBoards("password");
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "You should be allowed to get all boards with the right password");

        assertEquals(boards, response.getBody(),
                "The same list as the board repository should be returned");
        mockito.finishMocking();
    }
    @Test
    public void testGetBoardByIdEmpty() {
        when(boardRepository.findById(5L)).thenReturn(Optional.empty());

        var response = boardController.getBoardById(5L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "If the board is not found, the API should return Error 404");
        mockito.finishMocking();
    }
    @Test
    public void testGetBoardByIdFound() {
        Board board = new Board(5L, "Board title");
        when(boardRepository.findById(5L)).thenReturn(Optional.of(board));

        var response = boardController.getBoardById(5L);
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "If the board is found, it should return status code 200");

        assertEquals(board, response.getBody(),
                "If the board is found, it should return that board");
        mockito.finishMocking();
    }

    @Test
    public void testGetBoardByIdNotFound () {

        when(boardRepository.findById(5L)).thenReturn(Optional.empty());
        var response = boardController.getBoardById(5L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "This should not work because there is no board with the 5L id");
        mockito.finishMocking();
    }

    @AfterEach
        public void tearDown() {
            mockito.finishMocking();
        }
}
