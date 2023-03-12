package client.scenes;

import commons.Card;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler<MouseEvent> {

    private final MainCtrl mainCtrl;
    private final CardListViewCtrl cardListViewCtrl;
    private final CardView view;
    private Card card;

    public CardViewCtrl(MainCtrl mainCtrl, CardListViewCtrl cardListViewCtrl) {
        this.mainCtrl = mainCtrl;
        this.cardListViewCtrl = cardListViewCtrl;
        this.view = new CardView(this);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public CardView getView() {
        return this.view;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            Object source = event.getSource();

            if (source == view.getButtonUp()){
                // move card up
                cardListViewCtrl.moveCardUp(this.card);
            } else if (source == view.getButtonDown()) {
                // move card down
                cardListViewCtrl.moveCardDown(this.card);
            } else {
                MouseEvent mouseEvent = (MouseEvent) event;
                mainCtrl.showCard(card);
            }
        }
    }

}
