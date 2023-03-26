package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CardListView extends TitledPane {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardListViewCtrl controller;

    private final ObservableList<Card> cards;

    private ListView<Card> listView;

    /**
     * Constructs a CardListView.
     * This shouldn't be called manually. If you need to create a CardListView,
     * you create a CardListViewController and call getView().
     * @param boardOverviewCtrl the board overview controller
     * @param controller the controller to use.
     * @param cards the list of cards to render
     */
    public CardListView(BoardOverviewCtrl boardOverviewCtrl,
                        CardListViewCtrl controller,
                        ObservableList<Card> cards) {
        super();
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.controller = controller;
        // Only keep the cards that have the same id as this list.
        this.cards = cards.filtered(
                card -> card.getListId() == controller.getCardList().getId()
        );

        setDragEvents();
        createView();
    }

    private void setDragEvents() {
        setOnDragDetected(event -> {
            System.out.println("onDragDetected" + controller.getCardList().getId());

            /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(controller.getCardList().getId()));
            db.setContent(content);

            event.consume();
        });
        setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                if (event.getDragboard().getString().startsWith("c")) {
                    if (this.controller.getCards().length == 0) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    } else {
                        event.acceptTransferModes(TransferMode.NONE);
                    }
                } else {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }

            event.consume();
        });
        setOnDragEntered(event -> {
            if (event.getDragboard().getString().startsWith("c")) {
                if (this.controller.getCards().length == 0) {
                    setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                }
            } else {
                setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            event.consume();
        });
        setOnDragExited(event -> {
            System.out.println("onMouseDragExited" + event.getDragboard().getString());
            setBorder(null);
            event.consume();
        });
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                if (db.getString().startsWith("c")) {
                    long cardId = Long.parseLong(db.getString().substring(1));
                    this.controller.moveCard(cardId);
                    success = true;
                } else {
                    long listId = Long.parseLong(db.getString());
                    this.controller.moveList(listId);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    protected void createView() {

        CardList cardList = this.controller.getCardList();

        this.setCollapsible(false);
        this.setText(cardList.getTitle());
        this.setMaxHeight(Double.MAX_VALUE);

        listView = new ListView<>();

        CardListViewCtrl controller = this.controller;

        listView.setCellFactory(param -> {
            CardViewCtrl cardViewCtrl = new CardViewCtrl(boardOverviewCtrl, controller);
            return cardViewCtrl.getView();
        });
        listView.setItems(this.cards);

        listView.getSelectionModel().getSelectedItems().addListener(controller);
        this.setContent(listView);
    }

    /**
     * This unselects all selected cards in the list.
     */
    public void clearSelection() {
        this.listView.getSelectionModel().clearSelection();
    }

    public void highlightCard(Card card) {
        this.listView.getSelectionModel().select(card);
    }
}
