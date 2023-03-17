package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getBoardId() {
        // Test getBoardId
        Board board = new Board(1, null);
        assertEquals(1, board.getId());
    }

    @Test
    void getCardLists() {
        // Test getCardLists
        Board board = new Board(1, null);
        assertNull(board.getCardLists());
    }

    @Test
    void testEquals() {
        // Test equals
        Board board1 = new Board(1, null);
        Board board2 = new Board(1, null);
        assertTrue(board1.equals(board2));

        Board board3 = new Board(3, null);
        assertFalse(board1.equals(board3));

        assertFalse(board1.equals(null));
    }
}