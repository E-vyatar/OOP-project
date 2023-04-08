package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import commons.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

public class EditBoardCtrlTest {

    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private BoardOverviewCtrl boardOverviewCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private ClientConfig clientConfig;
    @InjectMocks
    private EditBoardCtrl editBoardCtrl;

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
        editBoardCtrl.close();
        verify(boardOverviewCtrl).hidePopup();
    }

    @Test
    public void testDelete() {
        when(serverUtils.getHostname()).thenReturn("http://example.com:4343");
        Board board = new Board(54L, "Board title");
        editBoardCtrl.setBoard(board);
        editBoardCtrl.delete();
        verify(serverUtils).deleteBoard(54L);
        verify(clientConfig).removeBoard("http://example.com:4343", 54L);
        verify(mainCtrl).saveConfig(notNull());
        verify(boardOverviewCtrl).hidePopup();
        verify(boardOverviewCtrl).returnToBoardList();
    }

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }
}
