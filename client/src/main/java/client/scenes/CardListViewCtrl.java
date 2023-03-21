package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class CardListViewCtrl implements ListChangeListener<Card> {
    private final MainCtrl mainCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardList cardList;
    private final CardListView view;
    private ObservableList<Card> cards;

    /**
     * CardListViewCtrl is the controller for viewing a CardList
     * and its cards. To get the CardListView, call {@link this.getView}
     * @param mainCtrl mainCtrl
     * @param boardOverviewCtrl boardOverviewCtl
     * @param cardList cardList for which it is used
     * @param cards cards to display
     */
    public CardListViewCtrl(MainCtrl mainCtrl,
                            BoardOverviewCtrl boardOverviewCtrl,
                            CardList cardList,
                            ObservableList<Card> cards) {
        this.mainCtrl = mainCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardList = cardList;
        // Only keep the cards that have the same id as this list.
        this.cards = cards;
        this.view = new CardListView(mainCtrl, this, cards);

        createView();
    }

    /**
     * Get the cardList attached to the CardListViewCtrl
     * @return the cardList for which CardListViewCtrl handles the logic
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * This creates the view.
     */
    private void createView() {
        this.view.createView();

        view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.showRenameList(cardList);
            }
        });
    }

    /**
     * Returns the view for which the controller handles the logic
     * @return the attached CardListView
     */
    public CardListView getView() {
        return this.view;
    }

    /**
     * This method moves a card one item up the list.
     * If it's the highest card, an error is shown.
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
}
