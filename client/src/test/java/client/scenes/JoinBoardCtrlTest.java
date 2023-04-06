package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

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

    // We can't test adding because that requires TextField
    // and we can't easily mock TextField (because it's not part of the constructor)

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }

}
