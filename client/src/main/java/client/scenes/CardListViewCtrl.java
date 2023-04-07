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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
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
    private CardListView view;

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
        // Only keep the cards that have the same id as this list.
        this.cards = FXCollections.observableList(cardList.getCards());

        this.view = new CardListView(boardOverviewCtrl, this, cards);

        createView();

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
     * Returns the view for which the controller handles the logic
     *
     * @return the attached CardListView
     */
    public CardListView getView() {
        return this.view;
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

    @SuppressWarnings("MissingJavadocMethod")
    public void highlightCard(Card card) {
        view.highlightCard(card);
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
}
