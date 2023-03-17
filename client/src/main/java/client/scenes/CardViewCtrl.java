package client.scenes;

import commons.Card;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler<MouseEvent> {

    private final MainCtrl mainCtrl;
    private final CardView view;
    private Card card;

    public CardViewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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
        if (event.getButton() == MouseButton.PRIMARY) {
            Object source = event.getSource();
            if (source instanceof Button &&
                "editButton".equals(((Button) source).getId())) {
                mainCtrl.showCard(card, true);
            } else {
                mainCtrl.showCard(card, false);
            }
        }
    }
}
