package client.scenes;

import commons.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class CardView extends ListCell<Card> {

    private final CardViewCtrl controller;

    public CardView(CardViewCtrl controller) {
        this.controller = controller;
    }

    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null) {
            this.controller.setCard(null);
            this.setGraphic(null);
        } else {

            AnchorPane pane = new AnchorPane();

            pane.setPrefHeight(150.0);
            pane.setPrefWidth(200.0);

            Button button = new Button();
            button.setId("editButton");
            button.setText("edit");
            button.setOnMouseClicked(this.controller);

            Label label = new Label();
            label.setText(card.getTitle());

            AnchorPane.setTopAnchor(button, 8.0);
            AnchorPane.setRightAnchor(button, 8.0);

            pane.getChildren().addAll(label, button);
            this.setGraphic(pane);
            this.setMinHeight(150.0);

            this.controller.setCard(card);
            pane.setOnMouseClicked(this.controller);

        }
    }

}
