package client.scenes;

import commons.CardList;
import javafx.fxml.FXML;

public class CardListViewCtrl {
    private final MainCtrl mainCtrl;
    private CardList cardList;
    private CardListView view;

    @FXML
    private String text;

    public CardListViewCtrl(MainCtrl mainCtrl, CardList cardList) {
        this.mainCtrl = mainCtrl;
        this.cardList = cardList;
        this.view = new CardListView(cardList, this);

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
}
