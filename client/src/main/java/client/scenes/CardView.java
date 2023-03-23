package client.scenes;

import commons.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CardView extends ListCell<Card> {

    private final CardViewCtrl controller;
    private Button buttonUp;
    private Button buttonDown;
    private Button editButton;

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
            // This contains the list of buttons and the rest of the card
            HBox hbox = new HBox();

            // Move card up and down list buttons
            VBox vbox = new VBox();

            this.buttonUp = new Button();
            this.buttonUp.setText("↑");
            this.buttonUp.setOnMouseClicked(this.controller);

            this.buttonDown = new Button();
            this.buttonDown.setText("↓");
            this.buttonDown.setOnMouseClicked(this.controller);

            vbox.getChildren().addAll(this.buttonUp, this.buttonDown);

            hbox.setPrefHeight(150.0);
            hbox.setPrefWidth(200.0);

            this.editButton = new Button();
            editButton.setId("editButton");
            editButton.setText("edit");
            editButton.setOnMouseClicked(this.controller);

            AnchorPane pane = new AnchorPane();

            Label label = new Label();
            label.setText(card.getTitle());

            AnchorPane.setTopAnchor(editButton, 8.0);
            AnchorPane.setRightAnchor(editButton, 8.0);

            pane.getChildren().addAll(label, editButton);

            hbox.getChildren().addAll(vbox, pane);
            hbox.setSpacing(8.0); // Put 8 pixels of space between buttons and the rest

            HBox.setHgrow(pane, Priority.ALWAYS);

            this.setGraphic(hbox);
            this.setMinHeight(150.0);

            this.controller.setCard(card);
            hbox.setOnMouseClicked(this.controller);

        }
    }

    public Button getButtonUp() {
        return buttonUp;
    }

    public Button getButtonDown() {
        return buttonDown;
    }

    public Button getEditButton() {
        return editButton;
    }
}
