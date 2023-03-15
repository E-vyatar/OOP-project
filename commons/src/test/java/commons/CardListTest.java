package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    @Test
    void getCardListId() {
        // Test getListId
        CardList cardList = new CardList(1, "2");
        assertEquals(1, cardList.getId());
    }

    @Test
    void getCardListTitle() {
        // Test getCardListTitle
        CardList cardList = new CardList(1, "2");
        assertEquals("2", cardList.getTitle());
    }

//    @Test
//    void getFirstCardId() {
//        // Test getFirstCardId
//        CardList cardList = new CardList(1, "2", 3);
//        assertEquals(3, cardList.getFirstCardId());
//    }

    @Test
    void testEquals() {
        // Test equals
        CardList cardList1 = new CardList(1, "2");
        CardList cardList2 = new CardList(1, "2");
        assertEquals(cardList1, cardList2);

        CardList cardList3 = new CardList(1, "2");
        assertNotEquals(cardList1, cardList3);

        assertNotEquals(null, cardList1);
    }
}