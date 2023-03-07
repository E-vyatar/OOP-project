package client.scenes;

import commons.BoardList;
import commons.Card;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

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

        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();

        var children = new ArrayList();

        for (Card card : this.cards) {
            CardViewCtrl cardViewCtrl = new CardViewCtrl(mainCtrl, card);
            children.add(cardViewCtrl.getView());
        }

        vBox.getChildren().addAll(children);

        scrollPane.setContent(vBox);
        // Setting the pref height is necessary for the scroll bar to show,
        // I'm not sure why, but this should be fixed so it's not static.
        scrollPane.setPrefHeight(360.0);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setContent(scrollPane);

        this.setMinWidth(200.0);

    }
}
