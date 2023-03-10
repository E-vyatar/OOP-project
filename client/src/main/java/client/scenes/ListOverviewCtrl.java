package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListOverviewCtrl implements Initializable {
    private final ServerUtils utils;
    private final MainCtrl mainCtrl;

    @FXML
    private HBox list_of_lists;

    @Inject
    public ListOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    public void refresh() {
        List<CardListView> cardList_list = new ArrayList<>();

        for (int i = 1; i <= 3; i ++) {
            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(mainCtrl, new CardList(i, "List"+i, 0));
            cardList_list.add(cardListViewCtrl.getView());
        }

        list_of_lists.getChildren().addAll(cardList_list);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
