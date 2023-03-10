package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;

public class CardPopupCtrl {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;

    public Card card;

    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;

    @Inject
    public CardPopupCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    public void setCard(Card card) {
        this.card = card;
        createView();
    }

    private void createView() {
        cardTitle.setText(card.getTitle());

        cardDescription.setText("Here there will be a description.");

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
}
