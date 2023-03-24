package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getBoardId() {
        // Test getBoardId
        Board board = new Board(1, "board", null);
        assertEquals(1, board.getId());
    }

    @Test
    void getCardLists() {
        // Test getCardLists
        Board board = new Board(1, "board", null);
        assertNull(board.getCardLists());
    }

    @Test
    void testEquals() {
        // Test equals
        Board board1 = new Board(1, "board", null);
        Board board2 = new Board(1, "board", null);
        assertTrue(board1.equals(board2));

        Board board3 = new Board(3, "board", null);
        assertFalse(board1.equals(board3));

        assertFalse(board1.equals(null));
    }
    @Test
    void testGetName() {
        Board board1 = new Board(1, "board 1", null);
        assertEquals("board 1", board1.getName());
        Board board2 = new Board(2, "board 2", null);
        assertEquals("board 2", board2.getName());
    }
    @Test
    void setName() {
        Board board1 = new Board(1, "untitled", null);
        board1.setName("Personal TODOs");
        assertEquals("Personal TODOs", board1.getName());
    }
}