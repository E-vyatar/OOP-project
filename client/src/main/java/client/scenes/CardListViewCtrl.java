package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class CardListViewCtrl {
    private final MainCtrl mainCtrl;
    private CardList cardList;
    private final CardListView view;
    private ObservableList<Card> cards;

    public CardListViewCtrl(MainCtrl mainCtrl, CardList cardList, ObservableList<Card> cards) {
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards;
        this.view = new CardListView(mainCtrl, this, cards);

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
        int indexOf = cards.indexOf(card);
        if (indexOf == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move upper card higher");
            alert.show();
        } else {
            // TODO: communicate with server
            cards.remove(indexOf);
            cards.add(indexOf - 1, card);
        }
    }
    public void moveCardDown(Card card) {
        int indexOf = cards.indexOf(card);
        if (indexOf + 1 == cards.size()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move bottom card lower");
            alert.show();
        } else {
            // TODO: communicate with server
            cards.remove(indexOf);
            cards.add(indexOf + 1, card);
        }
    }
}
