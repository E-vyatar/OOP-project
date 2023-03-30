package client.scenes;

import client.utils.CardsUtils;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddCardCtrlTest {

    AddCardCtrl addCardCtrl;

    @Mock
    ServerUtils serverUtilsMock;
    @Mock
    SocketsUtils socketsUtils;

    @Mock
    CardsUtils cardsUtilsMock;
    @Mock
    MainCtrl mainCtrlMock;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        addCardCtrl = new AddCardCtrl(serverUtilsMock,socketsUtils, cardsUtilsMock, mainCtrlMock);
    }

    @Test
    public void test() {

    }

}