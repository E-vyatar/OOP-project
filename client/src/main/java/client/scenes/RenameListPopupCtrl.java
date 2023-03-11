package client.scenes;

import client.utils.ServerUtils;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;

public class RenameListPopupCtrl {
    private final ServerUtils utils;
    private final MainCtrl mainCtrl;
    private CardList cardList;


    @FXML
    private TextField listTitle;

    @Inject
    public RenameListPopupCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }


    public void close(ActionEvent actionEvent) {
        mainCtrl.hideRenameListPopup();
    }

    public void save(ActionEvent actionEvent) {
        // TODO
        throw new NotImplementedException("This must be implemented later");
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }
}