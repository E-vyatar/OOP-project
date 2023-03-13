package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    @Test
    void getCardListId() {
        // Test getListId
        CardList cardList = new CardList(1, "2", 3);
        assertEquals(1, cardList.getId());
    }

    @Test
    void getCardListTitle() {
        // Test getCardListTitle
        CardList cardList = new CardList(1, "2", 3);
        assertEquals("2", cardList.getTitle());
    }

    @Test
    void getFirstCardId() {
        // Test getFirstCardId
        CardList cardList = new CardList(1, "2", 3);
        assertEquals(3, cardList.getFirstCardId());
    }

    @Test
    void testEquals() {
        // Test equals
        CardList cardList1 = new CardList(1, "2", 3);
        CardList cardList2 = new CardList(1, "2", 3);
        assertTrue(cardList1.equals(cardList2));

        CardList cardList3 = new CardList(1, "2", 16);
        assertFalse(cardList1.equals(cardList3));

        assertFalse(cardList1.equals(null));
    }
}