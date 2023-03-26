package client.scenes;

import client.utils.CardsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddCardCtrlTest {

    AddCardCtrl addCardCtrl;

    @Mock
    CardsUtils cardsUtilsMock;
    @Mock
    MainCtrl mainCtrlMock;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        addCardCtrl = new AddCardCtrl(cardsUtilsMock, mainCtrlMock);
    }

    @Test
    public void test() {

    }

}