package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CardListTest {

    @Test
    void getCardListId() {
        // Test getListId
        CardList cardList = new CardList("2", 0, 0);
        assertEquals(0, cardList.getId());
    }

    @Test
    void getCardListTitle() {
        // Test getCardListTitle
        CardList cardList = new CardList("2", 0, 0);
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
        CardList cardList1 = new CardList("2", 0, 0);
        CardList cardList2 = new CardList("2", 0, 0);
        assertEquals(cardList1, cardList2);

        CardList cardList3 = new CardList("3", 0, 0);
        assertNotEquals(cardList1, cardList3);

        assertNotEquals(null, cardList1);
    }
}