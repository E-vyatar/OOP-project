package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import commons.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.verify;

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

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }

}
