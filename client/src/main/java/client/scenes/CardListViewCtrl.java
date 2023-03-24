package client.scenes;

import client.FXConfig;
import client.FXMLInitializer;
import com.google.inject.Injector;
import commons.Card;
import commons.CardList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

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
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardList cardList;
    private final CardListView view;
    private final ObservableList<Card> cards;

    public void initialize(BoardOverviewCtrl boardOverviewCtrl, CardList cardList, ObservableList<Card> cards){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardList = cardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards;

        this.view = new CardListView(boardOverviewCtrl, this, cards);

        createView();
    }

    public CardList getCardList() {
        return cardList;
    }

    private void createView() {
        CardList cardList = this.getCardList();

        CardListViewCtrl controller = this;

        cardListView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                CardViewCtrl cardViewCtrl = new CardViewCtrl(boardOverviewCtrl, controller);
                return cardViewCtrl.getView();
            }
        });
        cardListView.setItems(this.cards);

        cardListView.getSelectionModel().getSelectedItems().addListener(controller);

        cardListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                boardOverviewCtrl.showRenameList(cardList);
            }
        });
    }

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

    public static CardListViewCtrl createNewCardListViewCtrl(BoardOverviewCtrl boardOverviewCtrl, CardList cardList, ObservableList<Card> cards){
        Injector INJECTOR = createInjector(new FXConfig());
        FXMLInitializer FXML = new FXMLInitializer(INJECTOR);

        var viewCtrl = FXML.load(CardListViewCtrl.class, "client", "scenes", "cardList.fxml");

        viewCtrl.getKey().initialize(boardOverviewCtrl, cardList, cards);

        return viewCtrl.getKey();
    }

    public AnchorPane getCardListNode() {
        return cardListNode;
    }

    public Card[] getCards() {
        return cards.toArray(new Card[0]);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        // TODO fix empty card bug
    }

    public void addCard(Card card, long index) {
        System.out.println("Adding card " + card + " at index " + index);
        card.setListId(cardList.getId());
        cards.add((int) index, card);
        card.setIdx(index);
    }

    public void moveList(long listId) {
        boardOverviewCtrl.moveList(listId, this.getCardList().getId());
    }

    public void moveCard(long cardId) {
        boardOverviewCtrl.moveCard(boardOverviewCtrl.getCard(cardId), getCardList(), getCards().length);
    }

    public void highlightCard(Card card) {
        view.highlightCard(card);
    }

    public void showAddCard() {
        boardOverviewCtrl.setCardListForShowAddCard(cardList);
    }
}
