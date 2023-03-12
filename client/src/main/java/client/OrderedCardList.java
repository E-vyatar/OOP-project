package client;

import commons.Card;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderedCardList {

    private LongProperty firstCardId;
    private ObservableList<Card> cards;

    /**
     * This constructs an OrderedCardList.
     * It takes the ID of the first card and a hashmap which maps the IDs of every card
     * to the respective card. The firstCardId is a LongProperty whose value is changed if
     * the pointer to the first card is changed (by moving a card).
     *
     * @param firstCardId the first card in the list
     * @param cardsMap hashmap containing all the cards
     */
    public OrderedCardList(LongProperty firstCardId, HashMap<Long, Card> cardsMap) {
        this.firstCardId = firstCardId;

        List<Card> list = new ArrayList<>();

        Card lastCard = cardsMap.get(firstCardId.getValue());
        while (lastCard != null) {
            list.add(lastCard);
            // Fix this when Card class is fixed
            if (lastCard.getNextCardId() == null) {
                break;
            }
            long nextCardId = Long.parseLong(lastCard.getNextCardId());
            lastCard = cardsMap.get(nextCardId);
        }
        this.cards = FXCollections.observableList(list);

    }

    /**
     * Gets the observable list of cards
     * @return ObservableList of cards
     */
    public ObservableList getObservableList() {
        return this.cards;
    }

    /**
     * Moves a card up the list (lower indices).
     * It also makes sure that all the pointers are still correct.
     * As of now, changes to the order of the list aren't synchronized with the server.
     *
     * @param card the card that you want to move up the last.
     * @throws IllegalArgumentException is thrown if it's the first card (since it can't move up)
     */
    public void moveCardUp(Card card) throws IllegalArgumentException{
        int index = this.cards.indexOf(card);
        if (index == 0) {
            throw new IllegalArgumentException("Can't move first card in list upwards");
        } else {
            this.cards.remove(card);
            this.cards.add(index - 1, card);
            // Set proper pointers
            if (index - 2 < 0) {
                // Old Situation:
                // HEADER -> previousCard -> Card ->
                // Desired situation:
                // Header -> Card -> PreviousCard ->
                Card previousCard = this.cards.get(1);
                previousCard.setNextCardId(card.getNextCardId());
                card.setNextCardId(previousCard.getId());
                this.firstCardId.set(Long.parseLong(card.getId()));

            } else {
                Card previousCard = this.cards.get(index - 2);
                Card nextCard = this.cards.get(index);
                // Now fix the pointers to the next card
                nextCard.setNextCardId(card.getNextCardId());
                card.setNextCardId(nextCard.getId());
                previousCard.setNextCardId(card.getId());
            }
        }
    }
    /**
     * Moves a card down the list (higher indices).
     * It also makes sure that all the pointers are still correct.
     * As of now, changes to the order of the list aren't synchronized with the server.
     *
     * @param card the card that you want to down the list.
     * @throws IllegalArgumentException is thrown if it's the last card (since it can't move down)
     */
    public void moveCardDown(Card card) throws IllegalArgumentException {
        int index = this.cards.indexOf(card);
        if (index == this.cards.size() - 1) {
            throw new IllegalArgumentException("Can't move last card in list down");
        } else {
            this.cards.remove(card);
            this.cards.add(index + 1, card);

            if (index == 0) {
                /*
                 * Set proper pointers
                 * starting situation:
                 *  header -> card -> next_card ->
                 * desired situation:
                 *  header -> next_card -> card ->
                 */
                Card nextCard = this.cards.get(0);
                this.firstCardId.set(Long.parseLong(nextCard.getId()));
                card.setNextCardId(nextCard.getNextCardId());
                nextCard.setNextCardId(card.getId());
            } else {
                /*
                 * Set proper pointers
                 * starting situation:
                 *  previousCard -> card -> next_card ->
                 * desired situation:
                 *  previous_card -> next_card -> card ->
                 */

                Card previousCard = this.cards.get(index - 1);
                Card nextCard = this.cards.get(index);

                card.setNextCardId(nextCard.getNextCardId());
                nextCard.setNextCardId(card.getId());
                previousCard.setNextCardId(nextCard.getId());
            }
        }
    }
}
