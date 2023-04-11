package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler<MouseEvent> {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardListViewCtrl cardListViewCtrl;
    private final CardView view;
    private Card card;

    /**
     * Constructs CardViewCtrl
     *
     * @param boardOverviewCtrl the board overview controller
     * @param cardListViewCtrl  the controller of the CardList of this card.
     */
    public CardViewCtrl(BoardOverviewCtrl boardOverviewCtrl, CardListViewCtrl cardListViewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardListViewCtrl = cardListViewCtrl;
        this.view = new CardView(this);
    }

    /**
     * Get the view of this controller
     *
     * @return the view
     */
    public CardView getView() {
        return this.view;
    }

    /**
     * The method handles the logic for when something in the card is clicked upon.
     * Depending on what happend (i.e. the {@param event } passed it will:
     * 1. move a card
     * 2. View a card (to edit or not to edit)
     * 3. Do nothing
     *
     * @param event the event which occurred
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Object source = event.getSource();
            boardOverviewCtrl.showCard(card, source == view.getEditButton());
        }
    }

    /**
     * Get the card of this controller
     *
     * @return the card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Set the card of this controller
     *
     * @param card the card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Get the BoardOverviewCtrl
     *
     * @return the board overview controller
     */
    public BoardOverviewCtrl getBoardOverviewCtrl() {
        return this.boardOverviewCtrl;
    }

    void addCardAt(Card card, long idx) {
        boardOverviewCtrl.requestMoveCard(card, cardListViewCtrl.getCardList(), idx);
    }

    /**
     * Get the cardlist of which the card is a part of
     *
     * @return the CardList
     */
    public CardList getCardList() {
        return cardListViewCtrl.getCardList();
    }
}
