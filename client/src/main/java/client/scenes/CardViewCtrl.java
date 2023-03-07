package client.scenes;

import commons.Card;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler {

    private final MainCtrl mainCtrl;
    private final Card card;
    private final CardView view;

    public CardViewCtrl(MainCtrl mainCtrl, Card card) {
        this.mainCtrl = mainCtrl;
        this.card = card;
        this.view = new CardView(this);
    }

    public Card getCard(){
        return this.card;
    }

    public CardView getView() {
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
