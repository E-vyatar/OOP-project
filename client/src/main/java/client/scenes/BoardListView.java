package client.scenes;

import commons.BoardList;
import commons.Card;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class BoardListView extends TitledPane {

    private BoardList boardList;

    public BoardListView(BoardList boardList) {
        super();
        this.boardList = boardList;

        createView();
    }

    private void createView() {

        this.setCollapsible(false);
        this.setText(boardList.title);

        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();

        var children = new ArrayList();

        for (Card card : boardList.getCards()) {
            CardView cardView = new CardView(card);
            children.add(cardView);
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
