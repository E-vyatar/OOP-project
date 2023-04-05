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

import static org.mockito.Mockito.verify;

public class JoinBoardCtrlTest {

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

    // We can't test adding because that requires TextField
    // and we can't easily mock TextField (because it's not part of the constructor)

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }

}
