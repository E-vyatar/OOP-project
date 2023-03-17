package client.scenes;

import commons.Card;
import commons.CardList;

import javafx.scene.control.TitledPane;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CardListView extends TitledPane {

    private final MainCtrl mainCtrl;
    private final CardListViewCtrl controller;

    private final FilteredList<Card> cards;
    private CardList cardList;


    public CardListView(MainCtrl mainCtrl, CardList cardList, CardListViewCtrl controller, ObservableList<Card> cards) {
        super();
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        this.controller = controller;
        // Only keep the cards that have the same id as this list.
        this.cards = cards.filtered(
                card -> card.getListId() == cardList.getId()
        );

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

        this.setCollapsible(false);
        this.setText(cardList.getTitle());
        this.setMaxHeight(Double.MAX_VALUE);

        ListView<Card> listView = new ListView<>();

        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                CardViewCtrl cardViewCtrl = new CardViewCtrl(mainCtrl);
                return cardViewCtrl.getView();
            }
        });
        listView.setItems(this.cards);

        this.setContent(listView);
    }
}
