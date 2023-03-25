package commons;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {
        card1 = new Card(0, 0, 0, "Title 1", 0);
        card2 = new Card(0, 0, "Title 1", 0);
        card3 = new Card(1, 0, 0, "Title 2", 0);
    }

    @Test
    void getId() {
        assertEquals(0, card1.getId());
    }

    @Test
    void setId() {
        card1.setId(1);
        assertEquals(1, card1.getId());
    }

    @Test
    void getListId() {
        assertEquals(0, card1.getListId());
    }

    @Test
    void setListId() {
        card1.setListId(1);
        assertEquals(1, card1.getListId());
    }

    @Test
    void getBoardId() {
        assertEquals(0, card1.getBoardId());
    }

    @Test
    void setBoardId() {
        card1.setBoardId(1);
        assertEquals(1, card1.getBoardId());
    }

    @Test
    void getTitle() {
        assertEquals("Title 1", card1.getTitle());
    }

    @Test
    void setTitle() {
        card1.setTitle("");
        assertEquals("", card1.getTitle());
    }

    @Test
    void getIdx() {
        assertEquals(0, card1.getIdx());
    }

    @Test
    void setIdx() {
        card1.setIdx(1);
        assertEquals(1, card1.getIdx());
    }

    @Test
    void testEquals() {
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(null, card1);
    }

    @Test
    void testHashCode() {
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Card{id=0, listId=0, title='Title 1', idx=0, boardId=0}", card1.toString());
    }
}