package client.scenes;

import commons.Card;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CardViewCtrl implements EventHandler<MouseEvent> {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardListViewCtrl cardListViewCtrl;
    private final CardView view;
    private Card card;

    public CardViewCtrl(BoardOverviewCtrl boardOverviewCtrl, CardListViewCtrl cardListViewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardListViewCtrl = cardListViewCtrl;
        this.view = new CardView(this);
    }

    public CardView getView() {
        return this.view;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Object source = event.getSource();

            if (source == view.getButtonUp()) {
                // move card up
                cardListViewCtrl.moveCardUp(this.card);
            } else if (source == view.getButtonDown()) {
                // move card down
                cardListViewCtrl.moveCardDown(this.card);
            } else boardOverviewCtrl.showCard(card, source == view.getEditButton());
        }
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public BoardOverviewCtrl getBoardOverviewCtrl() {
        return this.boardOverviewCtrl;
    }
}
