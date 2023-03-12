package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

import java.util.Objects;

public class BoardListView extends TitledPane {

    private final MainCtrl mainCtrl;
    private final FilteredList<Card> cards;
    private final CardList cardList;

    public BoardListView(MainCtrl mainCtrl, CardList cardList, ObservableList<Card> cards) {
        super();
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards.filtered(
                card -> Objects.equals(card.getListId(), "" + this.cardList.getCardListId())
        );

        createView();
    }

    private void createView() {

        this.setCollapsible(false);
        this.setText(cardList.getCardListTitle());
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

    public String getListName() {
        return this.cardList.getCardListTitle();
    }

}
