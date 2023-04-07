package client.scenes;

import client.FXConfig;
import client.FXMLInitializer;
import com.google.inject.Injector;
import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.google.inject.Guice.createInjector;

public class CardListViewCtrl implements ListChangeListener<Card> {
    private BoardOverviewCtrl boardOverviewCtrl;
    private CardList cardList;
    private ObservableList<Card> cards;
    @FXML
    private ListView<Card> cardListView;
    @FXML
    private Button editButton;
    @FXML
    private Button addCardButton;
    @FXML
    private AnchorPane cardListNode;
    @FXML
    private Text cardListTitle;

    /**
     * This constructs an instance of CardListViewCtrl.
     * CardListViewCtrl is the controller for viewing a CardList
     * and its cards. To get the CardListView, call {@link this.getView}
     *
     * @param boardOverviewCtrl boardOverviewCtrl
     * @param cardList          cardList for which it is used
     * @return the constructed CardListViewCtrl
     */
    @SuppressWarnings("LocalVariableName")
    public static CardListViewCtrl createNewCardListViewCtrl(
        BoardOverviewCtrl boardOverviewCtrl,
        CardList cardList) {

        Injector injector = createInjector(new FXConfig());
        FXMLInitializer FXMLInitializer = new FXMLInitializer(injector);

        var viewCtrl = FXMLInitializer.load(CardListViewCtrl.class,
            "client", "scenes", "cardList.fxml");

        viewCtrl.getKey().initialize(boardOverviewCtrl, cardList);

        return viewCtrl.getKey();
    }
    /**
     * Initialise the controller.
     * This includes creating the view.
     *
     * @param boardOverviewCtrl the board overview controller
     * @param cardList          the cardList
     */
    private void initialize(BoardOverviewCtrl boardOverviewCtrl,
                            CardList cardList) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardList = cardList;
        this.cards = FXCollections.observableList(cardList.getCards());

        createView();
        setDragEvents();

        addCardButton.setOnAction(event -> showAddCard());
    }

    /**
     * Get the cardList attached to the CardListViewCtrl
     *
     * @return the cardList for which CardListViewCtrl handles the logic
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * This creates the view.
     */
    private void createView() {
        CardListViewCtrl controller = this;

        cardListView.setCellFactory(param -> {
            CardViewCtrl cardViewCtrl = new CardViewCtrl(boardOverviewCtrl, controller);
            return cardViewCtrl.getView();
        });
        cardListView.setItems(this.cards);
        cardListTitle.setText(cardList.getTitle());

        cardListView.getSelectionModel().getSelectedItems().addListener(controller);
    }

    /**
     * Update card title
     */
    public void refresh() {
        cardListTitle.setText(cardList.getTitle());
    }

    /**
     * This method moves a card one item up the list.
     * If it's the highest card, an error is shown.
     *
     * @param card the card to move upwards
     */
    public void moveCardUp(Card card) {
        int indexOf = cards.indexOf(card);
        if (indexOf == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move upper card higher");
            alert.show();
        } else {
            // TODO: communicate with server
            cards.remove(indexOf);
            cards.add(indexOf - 1, card);
        }
    }

    /**
     * This method moves the card one item down the list.
     * If it's the bottom card, an error is shown.
     *
     * @param card the card to move downwards.
     */
    public void moveCardDown(Card card) {
        int indexOf = cards.indexOf(card);
        if (indexOf + 1 == cards.size()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't move bottom card lower");
            alert.show();
        } else {
            // TODO: communicate with server
            cards.remove(indexOf);
            cards.add(indexOf + 1, card);
        }
    }

    /**
     * This listens for changes in which card is selected.
     * In here we check if the change was that a card got selected,
     * and if so we make sure that it will be the only card that is selected.
     *
     * @param c an object representing the change that was done
     */
    @Override
    public void onChanged(Change<? extends Card> c) {
        if (c.next() && c.wasAdded()) {
            boardOverviewCtrl.unselectCards(this);
        }
    }

    /**
     * This unselects all selected cards in the list.
     */
    public void clearSelection() {
        cardListView.getSelectionModel().clearSelection();
    }

    /**
     * Get the root node of the CardListView
     *
     * @return the root node
     */
    public AnchorPane getCardListNode() {
        return cardListNode;
    }

    /**
     * Get the cards of this CardListViewCtrl
     *
     * @return an array of the cards
     */
    public Card[] getCards() {
        return cards.toArray(new Card[0]);
    }

    /**
     * Get the observable list of cards
     * @return the observable list of cards, updates are automatically propagated
     */
    public ObservableList<Card> getObservableCards() {
        return cards;
    }

    /**
     * Remove a card from the view
     *
     * @param card the card to remove
     */
    public void removeCard(Card card) {
        cards.remove(card);
        // TODO fix empty card bug
    }

    /**
     * Add a card
     *
     * @param card  the card to add
     * @param index where to add the card
     *              If index is negative the card will be added at the end of the list
     */
    public void addCard(Card card, long index) {
        if (index < 0) {
            index = cards.size();
        }
        System.out.println("Adding card " + card + " at index " + index);
        card.setListId(cardList.getId());
        cards.add((int) index, card);
        card.setIdx(index);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public void moveList(long listId) {
        boardOverviewCtrl.moveList(listId, this.getCardList().getId());
    }

    @SuppressWarnings("MissingJavadocMethod")
    public void moveCard(long cardId) {
        Card card = boardOverviewCtrl.getCard(cardId);
        boardOverviewCtrl.requestMoveCard(card, getCardList(), getCards().length);
    }

    /**
     * Set the CardList of the AddCard window and open the window
     */
    public void showAddCard() {
        boardOverviewCtrl.setCardListForShowAddCard(cardList);
        boardOverviewCtrl.showAddCard();
    }

    /**
     * Replace a card by its edited version
     * @param originalCardIdx The card's index in the list
     * @param editedCard The new version of the card
     */
    public void setCard(long originalCardIdx, Card editedCard) {
        cards.set((int) originalCardIdx, editedCard);
    }

    /**
     * Tells {@link BoardOverviewCtrl} to show RenameList Popup.
     * (It's used in {@link RenameListPopupCtrl})
     *
     */
    public void showRenameList() {
        boardOverviewCtrl.showRenameList(this);
    }

    /**
     * Sets the displayed title in the CardList view
     *
     * @param title the new title to be set
     */
    public void setTitle(String title) {
        cardList.setTitle(title);
        cardListTitle.setText(title);
    }

    /**
     * Sets the CardList for this controller
     *
     * @param cardList the new CardList for this controller
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    @SuppressWarnings({"MethodLength", "CyclomaticComplexity"})
    private void setDragEvents() {
        cardListView.setOnDragDetected(event -> {

            /* allow any transfer mode */
            Dragboard db = cardListNode.startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(cardList.getId()));
            db.setContent(content);

            event.consume();
        });
        cardListView.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() == this) {
                return;
            } else if (event.getDragboard().hasContent(CardView.CARD_DATA_FORMAT)) {
                // Only accept cards this way if the list doesn't have cards
                // This is necessary cause otherwise, there's no way to receive a card
                if (cards.size() == 0) {
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    event.acceptTransferModes(TransferMode.NONE);
                }
            } else if ( event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });
        cardListView.setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(CardView.CARD_DATA_FORMAT)) {
                if (cards.size() == 0) {
                    cardListView.setBorder(new Border(new BorderStroke(
                            Color.RED, BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                }
            } else {
                cardListView.setBorder(new Border(new BorderStroke(
                        Color.RED, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            event.consume();
        });
        cardListView.setOnDragExited(event -> {
            cardListView.setBorder(null);
            event.consume();
        });
        cardListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(CardView.CARD_DATA_FORMAT)) {
                long cardId = (Long) db.getContent(CardView.CARD_DATA_FORMAT);
                moveCard(cardId);
                success = true;
            } else if (db.hasString()) {
                long listId = Long.parseLong(db.getString());
                moveList(listId);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
