package client.scenes;

import commons.CardList;
import javafx.scene.control.TitledPane;

public class CardListView extends TitledPane {

    private final CardListViewCtrl controller;

    private CardList cardList;


    public CardListView(CardList cardList, CardListViewCtrl controller) {
        this.cardList = cardList;
        this.controller = controller;

        createView();
    }

    public void createView() {
        this.setCollapsible(false);
        this.setText(cardList.getCardListTitle());
    }

    public CardList getCardList() {
        return cardList;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
        createView();
    }
}
