package client.scenes;

import commons.Card;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler<MouseEvent> {

    private final MainCtrl mainCtrl;
    private final CardListViewCtrl cardListViewCtrl;
    private final CardView view;
    private Card card;

    /**
     * Constructs CardViewCtrl
     * @param mainCtrl the main controller
     * @param cardListViewCtrl the controller of the CardList of this card.
     */
    public CardViewCtrl(MainCtrl mainCtrl, CardListViewCtrl cardListViewCtrl) {
        this.mainCtrl = mainCtrl;
        this.cardListViewCtrl = cardListViewCtrl;
        this.view = new CardView(this);
    }

    /**
     * Set's the card of cardViewCtrl.
     * TODO: It seems this is never used, and probably wouldn't do anything
     * so it probably should be removed.
     * @param card the card to set
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Retrieve the view attached to the controller
     * @return the {@link CardView} for which the controller handles the logic.
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
     * @param event the event which occurred
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Object source = event.getSource();

            if (source == view.getButtonUp()){
                // move card up
                cardListViewCtrl.moveCardUp(this.card);
            } else if (source == view.getButtonDown()) {
                // move card down
                cardListViewCtrl.moveCardDown(this.card);
            } else if (source == view.getEditButton()) {
                mainCtrl.showCard(card, true);
            } else {
                mainCtrl.showCard(card, false);
            }
        }
    }

}
