package client;

import commons.Card;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderedCardListTest {

    private OrderedCardList orderedCardList;
    private LongProperty firstCardId;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;


    @BeforeEach
    void setUp() {
        card1 = new Card("1", null, null, "2", null);
        card2 = new Card("2", null, null, "3", null);
        card3 = new Card("3", null, null, "4", null);
        card4 = new Card("4", null, null, null, null);
        HashMap<Long, Card> cards = new HashMap();
        cards.put(1L, card1);
        cards.put(2L, card2);
        cards.put(3L, card3);
        cards.put(4L, card4);
        firstCardId = new SimpleLongProperty(1L);
        orderedCardList = new OrderedCardList(firstCardId, cards);
    }
    @Test
    void move_first_card_up(){
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderedCardList.moveCardUp(card1);
            }
        });
    }
    @Test
    void move_last_card_down(){
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderedCardList.moveCardDown(card4);
            }
        });
    }
    @Test
    void move_any_card_up() {

        orderedCardList.moveCardUp(card3);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card1, card3, card2, card4));
        // Check for the right pointers
        assertEquals(1L, firstCardId.get());
        assertEquals(card3.getId(), card1.getNextCardId());
        assertEquals(card2.getId(), card3.getNextCardId());
        assertEquals(card4.getId(), card2.getNextCardId());
        assertEquals(null, card4.getNextCardId());


        orderedCardList.moveCardUp(card4);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card1, card3, card4, card2));
        // Check for the right pointers
        assertEquals(1L, firstCardId.get());
        assertEquals(card3.getId(), card1.getNextCardId());
        assertEquals(card4.getId(), card3.getNextCardId());
        assertEquals(card2.getId(), card4.getNextCardId());
        assertEquals(null, card2.getNextCardId());
    }
    @Test
    void move_any_card_down() {

        orderedCardList.moveCardDown(card3);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card1, card2, card4, card3));
        // Check for the right pointers
        assertEquals(1L, firstCardId.get());
        assertEquals(card2.getId(), card1.getNextCardId());
        assertEquals(card4.getId(), card2.getNextCardId());
        assertEquals(card3.getId(), card4.getNextCardId());
        assertEquals(null, card3.getNextCardId());


        orderedCardList.moveCardDown(card4);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card1, card2, card3, card4));
        // Check for the right pointers
        assertEquals(1L, firstCardId.get());
        assertEquals(card2.getId(), card1.getNextCardId());
        assertEquals(card3.getId(), card2.getNextCardId());
        assertEquals(card4.getId(), card3.getNextCardId());
        assertEquals(null, card4.getNextCardId());
    }
    @Test
    void move_first_card_down() {
        orderedCardList.moveCardDown(card1);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card2, card1, card3, card4));
        // Check for the right pointers
        assertEquals(2L, firstCardId.get());
        assertEquals(card1.getId(), card2.getNextCardId());
        assertEquals(card3.getId(), card1.getNextCardId());
        assertEquals(card4.getId(), card3.getNextCardId());
        assertEquals(null, card4.getNextCardId());

    }
    @Test
    void move_second_card_up() {
        orderedCardList.moveCardUp(card2);
        // Check for right order in list
        assertEquals(orderedCardList.getObservableList(), List.of(card2, card1, card3, card4));
        // Check for the right pointers
        assertEquals(2L, firstCardId.get());
        assertEquals(card1.getId(), card2.getNextCardId());
        assertEquals(card3.getId(), card1.getNextCardId());
        assertEquals(card4.getId(), card3.getNextCardId());
        assertEquals(null, card4.getNextCardId());

    }

}