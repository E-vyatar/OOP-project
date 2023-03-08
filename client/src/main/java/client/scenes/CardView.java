package client.scenes;

import commons.Card;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

public class CardView extends ListCell<Card> {

    private final CardViewCtrl controller;

    public CardView(CardViewCtrl controller) {
        this.controller = controller;
    }

    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null){
            this.controller.setCard(null);
            this.setGraphic(null);
        } else {

            Pane pane = new Pane();

            pane.setPrefHeight(150.0);
            pane.setPrefWidth(200.0);

            Label label = new Label();
            label.setText(card.title);

            pane.getChildren().addAll(label);
            this.setGraphic(pane);
            this.setMinHeight(150.0);

            this.controller.setCard(card);
            pane.setOnMouseClicked(this.controller);

        }
    }

}
