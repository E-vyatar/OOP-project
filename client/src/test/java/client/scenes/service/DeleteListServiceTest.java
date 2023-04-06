package client.scenes.service;

import client.utils.SocketsUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link client.scenes.service.DeleteListService}
 * which is used in {@link client.scenes.DeleteListPopupCtrl}.
 */
class DeleteListServiceTest {
    private MockitoSession mockito;
    @Mock
    private SocketsUtils socketsUtils;
    @InjectMocks
    private DeleteListService deleteListService;

    /**
     * Starts the mocks.
     */
    @BeforeEach
    void setUp() {
        mockito = Mockito.mockitoSession()
            .strictness(Strictness.STRICT_STUBS)
            .initMocks(this)
            .startMocking();
    }

    /**
     * Closes the mocks.
     */
    @AfterEach
    void tearDown() {
        mockito.finishMocking();
    }

    /**
     * Tests the static generateLabelText() method.
     */
    @Test
    void generateLabelText() {
        assertEquals("Are you sure you want to delete 'List Name' list? " +
            "It will delete all cards within it.",
            DeleteListService.generateLabelText("List Name"));
        assertEquals("Are you sure you want to delete 'pngGz74DP4cs2RWUsgKQoPh4k0i7sv12h" +
                "hqWPBlltjxkKBgMct2hhqWPBllt...' list? " +
            "It will delete all cards within it.",
            DeleteListService.generateLabelText("pngGz74DP4cs2RWUsgKQoPh4k0i7sv12hhq" +
                "WPBlltjxkKBgMct2hhqWPBlltjxkKBgMctpngGz74DP4cs2RWUsgKQoPh4k0"));
    }

    /**
     * Tests the delete() method.
     */
    @Test
    void delete() {
        long listId = 0;
        deleteListService.setListId(listId);
        doNothing().when(socketsUtils).send("/app/lists/delete", listId);
        deleteListService.delete();
        verify(socketsUtils, times(1)).send("/app/lists/delete", listId);
    }

    /**
     * Tests the getListId() & setListId() methods.
     */
    @Test
    void getAndSetListId() {
        deleteListService.setListId(0);
        assertEquals(0, deleteListService.getListId());
        deleteListService.setListId(1);
        assertEquals(1, deleteListService.getListId());
    }
}