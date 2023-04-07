package client.scenes;

import commons.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardView extends ListCell<Card> {

    private final CardViewCtrl controller;
    private Button editButton;

    /**
     * This constructs a CardView
     *
     * @param controller the controller of the CardView
     */
    public CardView(CardViewCtrl controller) {
        this.controller = controller;

        setDragEvents();
    }

    @SuppressWarnings({"MethodLength", "CyclomaticComplexity"})
    private void setDragEvents() {

        // such a drag
        setOnDragDetected(event -> {
            if (controller.getCard() == null) {
                return;
            }

            /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("c" + controller.getCard().getId());
            db.setContent(content);

            event.consume();
        });
        setOnDragOver(event -> {
            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != this
                && event.getDragboard().hasString()
            ) {
                /* allow for both copying and moving, whatever user chooses */
                if (event.getDragboard().getString().startsWith("c")) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }

            event.consume();

        });
        setOnDragEntered(event -> {
            if (event.getGestureSource() != this
                && event.getDragboard().hasString()
                && event.getDragboard().getString().startsWith("c")
            ) {
                setBorder(new Border(new BorderStroke(
                    Color.RED, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            event.consume();
        });
        setOnDragExited(event -> {
            setBorder(null);
            event.consume();
        });
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                if (db.getString().startsWith("c")) {
                    long id = Long.parseLong(db.getString().substring(1));
                    Card card = controller.getBoardOverviewCtrl().getCard(id);

                    controller.addCardAt(card, getIndex());
                    success = true;

                } else {
                    long id = Long.parseLong(db.getString());
                    controller.getBoardOverviewCtrl()
                        .moveList(id, controller.getCardList().getId());
                    success = true;
                }
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }


    @Override
    @SuppressWarnings("MethodLength")
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);

        if (empty || card == null) {
            this.controller.setCard(null);
            this.setGraphic(null);
        } else {
            // This contains the list of buttons and the rest of the card
            HBox hbox = new HBox();

            hbox.setPrefHeight(150.0);
            hbox.setPrefWidth(200.0);

            this.editButton = new Button();
            editButton.setId("editButton");
            editButton.setText("edit");
            editButton.setOnMouseClicked(this.controller);
            // Set button width to match the width of its text
            editButton.setMinWidth(USE_PREF_SIZE);

            Label label = new Label();
            label.setText(card.getTitle());
            // Used to allow the label to change its width
            label.setMaxWidth(Double.MAX_VALUE);
            label.setWrapText(true);

            hbox.getChildren().addAll(label, editButton);
            hbox.setSpacing(8.0); // Put 8 pixels of space between buttons and the rest

            // Stretch the label to occupy all space not occupied other nodes in the HBox
            HBox.setHgrow(label, Priority.ALWAYS);

            this.setGraphic(hbox);
            this.setMinHeight(150.0);

            this.controller.setCard(card);
            hbox.setOnMouseClicked(this.controller);

        }
    }

    /**
     * Returns the button to edit a card
     *
     * @return the button to edit a card
     */
    public Button getEditButton() {
        return editButton;
    }
}
