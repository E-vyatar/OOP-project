package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class CardListViewCtrl implements ListChangeListener<Card> {
    private final MainCtrl mainCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final CardList cardList;
    private final CardListView view;

    public CardListViewCtrl(MainCtrl mainCtrl, BoardOverviewCtrl boardOverviewCtrl, CardList cardList, ObservableList<Card> cards) {
        this.mainCtrl = mainCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.cardList = cardList;
        this.view = new CardListView(mainCtrl, cardList, this, cards);

        createView();
    }

    private void createView() {
        view.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainCtrl.showRenameList(cardList);
            }
        });
    }

    public CardListView getView() {
        return this.view;
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
