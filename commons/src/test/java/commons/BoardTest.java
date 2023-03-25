package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board1;
    private Board board2;
    private Board board3;

    @BeforeEach
    void setUp() {
        board1 = new Board(0);
        board2 = new Board();
        board3 = new Board(2);
    }

    @Test
    void getId() {
        assertEquals(0, board1.getId());
    }

    @Test
    void setId() {
        board1.setId(2);
        assertEquals(2, board1.getId());
    }

    @Test
    void getCardLists() {
        assertEquals(new ArrayList<CardList>(), board1.getCardLists());
    }

    @Test
    void setCardLists() {
        List<CardList> list = new ArrayList<>();
        list.add(new CardList());

        board1.setCardLists(list);
        assertEquals(list, board1.getCardLists());
    }

    @Test
    void testEquals() {
        assertEquals(board1, board2);
        assertNotEquals(board1, board3);
        assertNotEquals(null, board1);
    }

    @Test
    void testHashCode() {
        assertEquals(board1.hashCode(), board2.hashCode());
        assertNotEquals(board1.hashCode(), board3.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Board{id=0, cardLists=[]}", board1.toString());
    }
}