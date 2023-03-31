package commons.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Tests for all methods in MoveCardMessage
public class MoveCardMessageTest {

    private MoveCardMessage moveCardMessage1;
    private MoveCardMessage moveCardMessage2;
    private MoveCardMessage moveCardMessage3;

    /**
     * Setup for the tests
     */
    @BeforeEach
    void setUp() {
        moveCardMessage1 = new MoveCardMessage(1, 2, 4);
        moveCardMessage2 = new MoveCardMessage(2, 3, 5);
        moveCardMessage3 = new MoveCardMessage(3, 4, 6);
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
        assertTrue(moveCardMessage1.equals(moveCardMessage1));
        assertTrue(moveCardMessage1.equals(new MoveCardMessage(1, 2, 4)));
        assertFalse(moveCardMessage1.equals(moveCardMessage2));
    }


}
