package client.scenes;

import commons.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardView extends ListCell<Card> {

    protected static final DataFormat CARD_DATA_FORMAT = new DataFormat("talio.card");

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
            Card card = controller.getCard();
            if (card == null) {
                return;
            }

            /* allow cards to be moved */
            Dragboard db = startDragAndDrop(TransferMode.MOVE);

            /* put the card on the dragboard */
            /* we put the id and not the card on the dragboard since it needs to be serializable */
            ClipboardContent content = new ClipboardContent();
            content.put(CARD_DATA_FORMAT, card.getId());
            db.setContent(content);

            event.consume();
        });
        setOnDragOver(event -> {
            /* accept it only if a card is being dragged and
             * if that card is not the card of this node */
            if (event.getGestureSource() != this
                && event.getDragboard().hasContent(CARD_DATA_FORMAT)
            ) {
                /* allow the user to only move cards */
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });
        setOnDragEntered(event -> {
            if (event.getGestureSource() != this
                && event.getDragboard().hasContent(CARD_DATA_FORMAT)
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

            if (db.hasContent(CARD_DATA_FORMAT)) {
                long id = (Long) db.getContent(CARD_DATA_FORMAT);
                Card draggedCard = controller.getBoardOverviewCtrl().getCard(id);

                // We want to put the card before the card we're hovering over.
                long newIdx = getIndex();
                if (newIdx >= controller.getCardList().getCards().size()) {
                    newIdx = controller.getCardList().getCards().size();
                }
                // If we move the card within the same list downwards,
                // we have to reduce the new index cause otherwise it gets put in the wrong position
                if (controller.getCardList().getId() == draggedCard.getListId()
                        && newIdx > controller.getCardList().getCards().indexOf(draggedCard)) {
                    newIdx--;
                }
                controller.addCardAt(draggedCard, newIdx);

                success = true;
            }

            /* let the source know whether the card was successfully
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
            this.setStyle("-fx-background-color: transparent");
        } else {
            this.setStyle("");
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
