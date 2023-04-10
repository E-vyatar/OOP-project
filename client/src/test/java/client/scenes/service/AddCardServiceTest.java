package client.scenes.service;

import client.utils.SocketsUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link client.scenes.service.AddCardService}
 * which is used in {@link client.scenes.AddCardCtrl}.
 */
class AddCardServiceTest {

    private MockitoSession mockito;
    @Mock
    private SocketsUtils socket;
    @InjectMocks
    private AddCardService addCardService;
    private final CardList cardList = new CardList(0, "List Name", 0, 0);

    /**
     * Starts the mocks.
     */
    @BeforeEach
    void setUp() {
        mockito = Mockito.mockitoSession()
            .strictness(Strictness.STRICT_STUBS)
            .initMocks(this)
            .startMocking();
        addCardService.setCardList(cardList);
    }

    /**
     * Closes the mocks.
     */
    @AfterEach
    void tearDown() {
        mockito.finishMocking();
    }

    /**
     * Test for getCardList() method
     */
    @Test
    void getCardList() {
        assertEquals(cardList, addCardService.getCardList());
    }

    /**
     * Test for setCardList() method
     */
    @Test
    void setCardList() {
        CardList newList = new CardList(1, "New List", 0, 0);
        addCardService.setCardList(newList);
        assertEquals(newList, addCardService.getCardList());
    }

    /**
     * Test for createAndSend() method
     */
    @Test
    void createAndSend() {
        Card card = new Card(0, 0, "Title", 0);
        doNothing().when(socket).send("/app/cards/new", card);
        addCardService.createAndSend("Title");
        verify(socket, times(1))
            .send("/app/cards/new", card);
        verifyNoMoreInteractions(socket);
    }
}