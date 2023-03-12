package client.scenes;

import commons.Card;
import commons.CardList;

import javafx.scene.control.TitledPane;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CardListView extends TitledPane {

    private final MainCtrl mainCtrl;
    private final CardListViewCtrl controller;

    private final ObservableList<Card> cards;


    public CardListView(MainCtrl mainCtrl, CardListViewCtrl controller, ObservableList<Card> cards) {
        super();
        this.mainCtrl = mainCtrl;
        this.controller = controller;
        this.cards = cards;
    }

    protected void createView() {

        CardList cardList = this.controller.getCardList();

        this.setCollapsible(false);
        this.setText(cardList.getCardListTitle());
        this.setMaxHeight(Double.MAX_VALUE);

        ListView<Card> listView = new ListView<>();

        CardListViewCtrl controller = this.controller;

        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                CardViewCtrl cardViewCtrl = new CardViewCtrl(mainCtrl, controller);
                return cardViewCtrl.getView();
            }
        });
        listView.setItems(this.cards);

        this.setContent(listView);
    }
}
