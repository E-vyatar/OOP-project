package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;
import java.util.List;

public class CardPopupCtrl {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;

    public Card card;

    @FXML
    private TextField cardTitle;

    @FXML
    private ChoiceBox list;
    @FXML
    private TextArea cardDescription;

    @Inject
    public CardPopupCtrl(ServerUtils utils, MainCtrl mainCtrl, BoardOverviewCtrl boardOverviewCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    public void setCard(Card card) {
        this.card = card;
        createView();
    }

    private void createView() {
        cardTitle.setText(card.getTitle());

        cardDescription.setText("Here there will be a description.");
        refresh();

    }

    @FXML
    private void close() {
        mainCtrl.hideCard();
    }

    @FXML
    private void save() {
        // TODO: allow to save data when editing card
        throw new NotImplementedException("Saving changes hasn't been implemented yet.");
    }

    public void refresh() {
        list.getItems().clear();
        List<String> listsNames = boardOverviewCtrl.getListsNames();
        for (String name : listsNames) {
            list.getItems().add(name);
        }
        // temporary, eventually needs to find actual list name from database
        String currentList = " list " + card.getListId();
        list.getSelectionModel().select(currentList);
    }

}
