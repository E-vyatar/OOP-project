package commons;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    private CardList cardList1;
    private CardList cardList2;
    private CardList cardList3;

    @BeforeEach
    void setUp() {
        cardList1  = new CardList(0, "Title 1", 0, 0);
        cardList2 = new CardList("Title 1", 0, 0);
        cardList3 = new CardList(1, "Title 2", 1, 0);
    }

    @Test
    void getId() {
        assertEquals(0, cardList1.getId());
    }

    @Test
    void setId() {
        cardList1.setId(1);
        assertEquals(1, cardList1.getId());
    }

    @Test
    void getBoardId() {
        assertEquals(0, cardList1.getBoardId());
    }

    @Test
    void setBoardId() {
        cardList1.setBoardId(1);
        assertEquals(1, cardList1.getBoardId());
    }

    @Test
    void getTitle() {
        assertEquals("Title 1", cardList1.getTitle());
    }

    @Test
    void setTitle() {
        cardList1.setTitle("Title 2");
        assertEquals("Title 2", cardList1.getTitle());
    }

    @Test
    void getIdx() {
        assertEquals(0, cardList1.getIdx());
    }

    @Test
    void setIdx() {
        cardList1.setIdx(1);
        assertEquals(1, cardList1.getIdx());
    }

    @Test
    void getCards() {
        assertEquals(new ArrayList<Card>(), cardList1.getCards());
    }

    @Test
    void setCards() {
        List<Card> list = new ArrayList<>();
        list.add(new Card());

        cardList1.setCards(list);
        assertEquals(list, cardList1.getCards());
    }

    @Test
    void testEquals() {
        assertEquals(cardList1, cardList2);
        assertNotEquals(cardList1, cardList3);
        assertNotEquals(null, cardList1);
    }

    @Test
    void testHashCode() {
        assertEquals(cardList1.hashCode(), cardList2.hashCode());
        assertNotEquals(cardList1.hashCode(), cardList3.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("CardList{id=0, title='Title 1', idx=0, boardId=0, cards=[]}", cardList1.toString());
    }
}