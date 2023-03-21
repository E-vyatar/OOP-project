package client.scenes;

import commons.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;

public class CardView extends ListCell<Card> {

    private final CardViewCtrl controller;
    private HBox hbox;
    private Button buttonUp;
    private Button buttonDown;
    private Button editButton;
    private Label cardTitle;

    /**
     * This constructs a CardView
     * @param controller the controller of the CardView
     */
    public CardView(CardViewCtrl controller) {
        this.controller = controller;
        createView();
    }

    /**
     * This method creates the view, i.e. it creates
     * all the javafx nodes like Button and HBox.
     */
    public void createView() {
        // This contains the list of buttons and the rest of the card
        hbox = new HBox();

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

        this.cardTitle = new Label();

        AnchorPane.setTopAnchor(editButton, 8.0);
        AnchorPane.setRightAnchor(editButton, 8.0);

        pane.getChildren().addAll(cardTitle, editButton);

        hbox.getChildren().addAll(vbox, pane);
        hbox.setSpacing(8.0); // Put 8 pixels of space between buttons and the rest

        HBox.setHgrow(pane, Priority.ALWAYS);

        this.setGraphic(hbox);
        this.setMinHeight(150.0);

        hbox.setOnMouseClicked(this.controller);
    }

    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null) {
            this.controller.setCard(null);
            this.setGraphic(null);
        } else {
            cardTitle.setText(card.getTitle());

            this.setGraphic(hbox);

            this.controller.setCard(card);
        }
    }

    /**
     * Returns the button to move a card up
     * @return the button to move a card up
     */
    public Button getButtonUp() {
        return buttonUp;
    }

    /**
     * Returns the button to move a card down
     * @return the button to move a card down
     */
    public Button getButtonDown() {
        return buttonDown;
    }

    /**
     * Returns the button to edit a card
     * @return the button to edit a card
     */
    public Button getEditButton() {
        return editButton;
    }
}
