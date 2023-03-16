package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddCardCtrlTest {

    AddCardCtrl addCardCtrl;

    @Mock
    ServerUtils serverUtilsMock;

    @Mock
    MainCtrl mainCtrlMock;

    @Mock
    BoardOverviewCtrl boardOverviewCtrlMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        addCardCtrl = new AddCardCtrl(serverUtilsMock,mainCtrlMock,boardOverviewCtrlMock);
    }

    @Test
    public void test() {

    }

}