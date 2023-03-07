package client.scenes;

import commons.Card;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EmbeddedCardViewCtrl implements EventHandler {

    private final MainCtrl mainCtrl;
    private final Card card;
    private final EmbeddedCardView view;

    public EmbeddedCardViewCtrl(MainCtrl mainCtrl, Card card) {
        this.mainCtrl = mainCtrl;
        this.card = card;
        this.view = new EmbeddedCardView(this);
    }

    public Card getCard(){
        return this.card;
    }

    public EmbeddedCardView getView() {
        return this.view;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent){
            MouseEvent mouseEvent = (MouseEvent) event;
            mainCtrl.showCard(card);
        }
    }
}
