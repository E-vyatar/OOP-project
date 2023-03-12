package client.scenes;

import client.OrderedCardList;
import commons.Card;
import commons.CardList;
import javafx.scene.control.Alert;

public class CardListViewCtrl {
    private final MainCtrl mainCtrl;
    private CardList cardList;
    private final CardListView view;
    private OrderedCardList cards;

    public CardListViewCtrl(MainCtrl mainCtrl, CardList cardList, OrderedCardList cards) {
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards;
        this.view = new CardListView(mainCtrl, this, cards.getObservableList());

        createView();
    }

    public CardList getCardList() {
        return cardList;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
        createView();
    }

    private void createView() {
        this.view.createView();

        view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.showRenameList(cardList);
            }
        });
    }


    public CardListView getView() {
        return this.view;
    }

    public void moveCardUp(Card card) {
        try {
            this.cards.moveCardUp(card);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move upper card higher");
            alert.show();
        }
    }
    public void moveCardDown(Card card) {
        try {
            this.cards.moveCardDown(card);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move bottom card lower");
            alert.show();
        }
    }
}
