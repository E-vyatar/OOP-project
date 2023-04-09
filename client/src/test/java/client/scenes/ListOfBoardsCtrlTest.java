package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import commons.Board;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ListOfBoardsCtrlTest {

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private SocketsUtils socketsUtils;
    @Mock
    private ClientConfig clientConfig;

    @InjectMocks
    private ListOfBoardsCtrl listOfBoardsCtrl;

    private MockitoSession mockito;

    @BeforeEach
    public void setup() {
        mockito = Mockito.mockitoSession()
                .strictness(Strictness.STRICT_STUBS)
                .initMocks(this)
                .startMocking();
    }

    @Test
    public void testDisconnect(){
        listOfBoardsCtrl.disconnect(null);
        verify(socketsUtils).disconnect();
        verify(mainCtrl).showConnect();
    }
    @Test
    public void deleteBoard() {
        Board board = new Board(91L, "Board title");
        listOfBoardsCtrl.deleteBoard(board);
        verify(serverUtils).deleteBoard(91L);
    }

    @Test
    public void addBoard() {
        listOfBoardsCtrl.joinBoard();
        verify(mainCtrl).showJoinBoard();
    }
    @Test
    public void testCreateBoard() {
        listOfBoardsCtrl.newBoard();
        verify(mainCtrl).showCreateBoard();
    }
    @Test
    public void testRemoveDeletedBoards() {
        List<Long> boardIds = new ArrayList<>();
        boardIds.addAll(List.of(3L, 9L, 14L, 4L));
        when(serverUtils.getHostname()).thenReturn("http://localhost:8080");
        when(clientConfig.getIds(notNull())).thenReturn(boardIds);

        Board board1 = new Board(3L, "board");
        Board board2 = new Board(14L, "board");
        listOfBoardsCtrl.removeDeletedBoards(List.of(board1, board2));

        assertEquals(List.of(3L, 14L), boardIds);

        // two boards were removed, so this should be in the message
        verify(mainCtrl).showAlert(eq(Alert.AlertType.INFORMATION), contains("2"), contains("2"));
        verify(mainCtrl).saveConfig(notNull());
    }
    @Test
    public void testRemoveDeletedBoardsNothingDeleted() {
        List<Long> boardIds = new ArrayList<>();
        boardIds.addAll(List.of(3L, 9L, 14L, 4L));
        when(serverUtils.getHostname()).thenReturn("http://localhost:8080");
        when(clientConfig.getIds(notNull())).thenReturn(boardIds);

        Board board1 = new Board(3L, "board");
        Board board2 = new Board(9L, "board");
        Board board3 = new Board(14L, "board");
        Board board4 = new Board(4L, "board");
        listOfBoardsCtrl.removeDeletedBoards(List.of(board1, board2, board3, board4));

        assertEquals(List.of(3L, 9L, 14L, 4L), boardIds);

        // nothing was removed, so there shouldn't be a pop-up
    }

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }

}
