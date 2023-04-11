package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JoinBoardCtrlTest {

    private static final String LOCALHOST = "http://localhost:8080";

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private ClientConfig clientConfig;

    @InjectMocks
    private JoinBoardCtrl joinBoardCtrl;

    private MockitoSession mockito;

    @BeforeEach
    public void setup() {
        mockito = Mockito.mockitoSession()
                .strictness(Strictness.STRICT_STUBS)
                .initMocks(this)
                .startMocking();
    }

    @Test
    public void testCancel(){
        joinBoardCtrl.cancel();
        verify(mainCtrl).showListOfBoards();
    }

    @Test
    public void testJoinBoard() {
        when(serverUtils.getHostname()).thenReturn(LOCALHOST);
        when(clientConfig.hasBoard(LOCALHOST, 19L)).thenReturn(false);
        when(serverUtils.boardExists(19L)).thenReturn(true);

        joinBoardCtrl.joinBoard(19L);

        verify(clientConfig).addBoard(LOCALHOST, 19L);
        verify(mainCtrl).saveConfig(isNotNull());
        verify(mainCtrl).showListOfBoards();
    }
    @Test
    public void testJoinJoinedBoard() {
        when(serverUtils.getHostname()).thenReturn(LOCALHOST);
        when(clientConfig.hasBoard(LOCALHOST, 31L)).thenReturn(true);

        joinBoardCtrl.joinBoard(31L);

        // You should get an error if you try to join a board you've already joined
        verify(mainCtrl).showAlert(eq(Alert.AlertType.ERROR), isNotNull(), isNotNull());
    }
    @Test
    public void testJoinNonExistingBoard() {
        when(serverUtils.getHostname()).thenReturn(LOCALHOST);
        when(clientConfig.hasBoard(LOCALHOST, 19L)).thenReturn(false);
        when(serverUtils.boardExists(19L)).thenReturn(false);

        joinBoardCtrl.joinBoard(19L);

        // You should get an error if you try to join a board that doesn't exist
        verify(mainCtrl).showAlert(eq(Alert.AlertType.ERROR), isNotNull(), isNotNull());
    }

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }

}
