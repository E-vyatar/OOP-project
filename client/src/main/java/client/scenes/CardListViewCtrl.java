package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class CardListViewCtrl implements ListChangeListener<Card> {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardList cardList;
    private final CardListView view;
    private ObservableList<Card> cards;

    public CardListViewCtrl(BoardOverviewCtrl boardOverviewCtrl, CardList cardList, ObservableList<Card> cards) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardList = cardList;
        // Track changes made to the list,
        // Note that it only keeps track of changes to the observable list,
        // not the original list.
        // TODO: investigate whether we can (and should) use an ObservableList in commons
        this.cards = FXCollections.observableList(cardList.getCards());
        this.view = new CardListView(boardOverviewCtrl, this, cards);
    }

    public CardList getCardList() {
        return cardList;
    }

    private void createView() {
        this.view.createView();

        view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                boardOverviewCtrl.showRenameList(cardList);
            }
        });
    }

    public CardListView getView() {
        return this.view;
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
        this.getView().clearSelection();
    }

    public void showAddCard() {
        boardOverviewCtrl.setCardListForShowAddCard(cardList);
    }
}
