package client.scenes.service;

import client.utils.SocketsUtils;
import commons.CardList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link client.scenes.service.RenameListService}
 * which is used in {@link client.scenes.RenameListPopupCtrl}.
 */
class RenameListServiceTest {

    private MockitoSession mockito;
    @Mock
    private SocketsUtils socketsUtils;
    @InjectMocks
    private RenameListService renameListService;
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
        renameListService.setCardList(cardList);
    }

    /**
     * Closes the mocks.
     */
    @AfterEach
    void tearDown() {
        mockito.finishMocking();
    }

    /**
     * Tests the save() method.
     */
    @Test
    void save() {
        doNothing().when(socketsUtils).send("/app/lists/edit", cardList);
        renameListService.save("POOP");
        assertEquals("POOP", renameListService.getTitle());
        verify(socketsUtils, times(1)).send("/app/lists/edit", cardList);
        verifyNoMoreInteractions(socketsUtils);
    }

    /**
     * Tests the setCardList() method.
     */
    @Test
    void setCardList() {
        assertEquals("List Name", renameListService.getTitle());
        assertEquals(0, renameListService.getListId());
        CardList newCardList = new CardList(1, "New Name", 0, 0);
        renameListService.setCardList(newCardList);
        assertEquals("New Name", renameListService.getTitle());
        assertEquals(1, renameListService.getListId());
        verifyNoInteractions(socketsUtils);
    }

    /**
     * Tests the getTitle() method.
     */
    @Test
    void getTitle() {
        assertEquals("List Name", renameListService.getTitle());
        verifyNoInteractions(socketsUtils);
    }

    /**
     * Tests the getListId() method.
     */
    @Test
    void getListId() {
        assertEquals(0, renameListService.getListId());
        verifyNoInteractions(socketsUtils);
    }
}