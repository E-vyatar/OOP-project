package client.scenes;

import commons.BoardList;
import commons.Card;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

public class BoardListView extends TitledPane {

    private final MainCtrl mainCtrl;
    private BoardList boardList;
    private final FilteredList<Card> cards;

    public BoardListView(MainCtrl mainCtrl, BoardList boardList, ObservableList<Card> cards) {
        super();
        this.mainCtrl = mainCtrl;
        this.boardList = boardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards.filtered(
                card -> card.listID == this.boardList.id
        );

        createView();
    }

    private void createView() {

        this.setCollapsible(false);
        this.setText(boardList.title);
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
