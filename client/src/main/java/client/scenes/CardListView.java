package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

public class CardListView extends TitledPane {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardListViewCtrl controller;

    private final ObservableList<Card> cards;

    private ListView<Card> listView;

    public CardListView(BoardOverviewCtrl boardOverviewCtrl, CardListViewCtrl controller, ObservableList<Card> cards) {
        super();
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.controller = controller;
        // Only keep the cards that have the same id as this list.
        this.cards = cards.filtered(
                card -> card.getListId() == controller.getCardList().getId()
        );

        createView();
    }

    protected void createView() {

        CardList cardList = this.controller.getCardList();

        this.setCollapsible(false);
        this.setText(cardList.getTitle());
        this.setMaxHeight(Double.MAX_VALUE);

        listView = new ListView<>();

        CardListViewCtrl controller = this.controller;

        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                CardViewCtrl cardViewCtrl = new CardViewCtrl(boardOverviewCtrl, controller);
                return cardViewCtrl.getView();
            }
        });
        listView.setItems(this.cards);

        listView.getSelectionModel().getSelectedItems().addListener(controller);
        this.setContent(listView);
    }

    /**
     * This unselects all selected cards in the list.
     */
    public void clearSelection() {
        this.listView.getSelectionModel().clearSelection();
    }
}
