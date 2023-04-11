package commons.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for all methods in MoveCardMessage
public class MoveCardMessageTest {

    private MoveCardMessage moveCardMessage1;
    private MoveCardMessage moveCardMessage2;

    /**
     * Setup for the tests
     */
    @BeforeEach
    void setUp() {
        moveCardMessage1 = new MoveCardMessage(1, 2, 8, 4);
        moveCardMessage2 = new MoveCardMessage(2, 3, 9, 5);
    }

    /**
     * Test for getCardId()
     */
    @Test
    void getCardId() {
        assertEquals(1, moveCardMessage1.getCardId());
    }

    /**
     * Test for getNewListId()
     */
    @Test
    void getNewListId() {
        assertEquals(2, moveCardMessage1.getNewListId());
    }

    /**
     * Test for getOldListId()
     */
    @Test
    void getOldListId() {
        assertEquals(8, moveCardMessage1.getOldListId());
    }

    /**
     * Test for getNewIndex()
     */
    @Test
    void getNewIndex() {
        assertEquals(4, moveCardMessage1.getNewIndex());
    }

    /**
     * Test for setCardId()
     */
    @Test
    void setCardId() {
        moveCardMessage1.setCardId(2);
        assertEquals(2, moveCardMessage1.getCardId());
    }

    /**
     * Test for setNewListId()
     */
    @Test
    void setNewListId() {
        moveCardMessage1.setNewListId(3);
        assertEquals(3, moveCardMessage1.getNewListId());
    }

    /**
     * Test for setNewListId()
     */
    @Test
    void setOldListId() {
        moveCardMessage1.setOldListId(3);
        assertEquals(3, moveCardMessage1.getOldListId());
    }

    /**
     * Test for setNewIndex()
     */
    @Test
    void setNewIndex() {
        moveCardMessage1.setNewIndex(5);
        assertEquals(5, moveCardMessage1.getNewIndex());
    }

    /**
     * Test for equals()
     */
    @Test
    void equals() {
        assertEquals(moveCardMessage1, moveCardMessage1);
        assertEquals(moveCardMessage1,
            new MoveCardMessage(1, 2, 8, 4));
        assertNotEquals(moveCardMessage1, moveCardMessage2);
    }

    /**
     * Test for setMoved()
     */
    @Test
    void setMoved() {
        moveCardMessage1.setMoved(true);
        assertTrue(moveCardMessage1.isMoved());
    }

    /**
     * Test for isMoved()
     */
    @Test
    void isMoved() {
        assertFalse(moveCardMessage1.isMoved());
    }

    @Test
    void testHashCode() {
        assertEquals(moveCardMessage1.hashCode(),
            new MoveCardMessage(1, 2, 8, 4).hashCode());
        assertNotEquals(new MoveCardMessage(1,4,7,4,true).hashCode(), moveCardMessage1.hashCode());
    }

}
