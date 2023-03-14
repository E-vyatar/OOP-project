package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ObservableList;

public class CardListViewCtrl {
    private final MainCtrl mainCtrl;
    private final CardList cardList;
    private final CardListView view;

    public CardListViewCtrl(MainCtrl mainCtrl, CardList cardList, ObservableList<Card> cards) {
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        this.view = new CardListView(mainCtrl, cardList, this, cards);

        createView();
    }

    private void createView() {
        view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.showRenameList(cardList);
            }
        });
    }


    public CardListView getView() {
        return this.view;
    }
}
