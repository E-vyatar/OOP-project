// TODO: Fix tests for the Card class
//package commons;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CardTest {
//
//    @Test
//    void getCardId() {
//        //test GetCardId
//        Card card = new Card(1, 2, "3", 4, 5);
//        assertEquals(1, card.getId());
//    }
//
//    @Test
//    void getListId() {
//        //test GetCardListId
//        Card card = new Card(1, 2, "3", 4, 5);
//        assertEquals(2, card.getListId());
//    }
//
//    @Test
//    void getCardTitle() {
//        //test GetCardTitle
//        Card card = new Card(1, 2, "3", 4, 5);
//        assertEquals("3", card.getTitle());
//    }
//
//    @Test
//    void getNextCardId() {
//        //test GetNextCardId
//        Card card = new Card(1, 2, "3", 4, 5);
//        assertEquals(4, card.getNextCardId());
//    }
//
//    @Test
//    void getBoardId() {
//        //test GetBoardId
//        Card card = new Card(1, 2, "3", 4, 5);
//        assertEquals(5, card.getBoardId());
//    }
//
//    @Test
//    void testEquals() {
//        //test Equals
//        Card card1 = new Card(1, 2, "3", 4, 5);
//        Card card2 = new Card(1, 2, "3", 4, 5);
//        assertTrue(card1.equals(card2));
//
//        Card card3 = new Card(1, 2, "3", 4, 16);
//        assertFalse(card1.equals(card3));
//
//        assertFalse(card1.equals(null));
//
//
//    }
//}