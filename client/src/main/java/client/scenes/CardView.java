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
    private Button buttonUp;
    private Button buttonDown;
    private Button editButton;

    public CardView(CardViewCtrl controller) {
        this.controller = controller;

        // such a drag
        setOnDragDetected(event -> {
            System.out.println("onDragDetected" + controller.getCard().getId());

            /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(controller.getCard().getId()));
            db.setContent(content);

            event.consume();
        });
        setOnDragOver(event -> {
            /* data is dragged over the target */
            System.out.println("onDragOver" + event.getDragboard().getString());

            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });
        setOnDragEntered(event -> {
            System.out.println("onMouseDragEntered" + event.getDragboard().getString());
            setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            event.consume();
        });
        setOnDragExited(event -> {
            System.out.println("onMouseDragExited" + event.getDragboard().getString());
            setBorder(null);
            event.consume();
        });
        setOnDragDropped(event -> {
            /* data dropped */
            System.out.println("onDragDropped" + event.getDragboard().getString());
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                long id = Long.parseLong(db.getString());
                Card card = controller.getBoardOverviewCtrl().getCard(id);
                addCardAfter(card);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    private void addCardAfter(Card card) {
        var thisCard = controller.getCard();
        var list = controller.getBoardOverviewCtrl().getCardListViewCtrl(thisCard.getListId());
        list.addCardAfter(thisCard, card);

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
