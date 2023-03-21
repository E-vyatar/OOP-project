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

    /**
     * This constructs the controller for the pop-up to rename a list.
     * @param utils the server utils
     * @param mainCtrl the main controller
     */
    @Inject
    public RenameListPopupCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }


    /**
     * This closses the pop-up to rename the list.
     * @param actionEvent not sure what this is
     */
    public void close(ActionEvent actionEvent) {
        mainCtrl.hideRenameListPopup();
    }

    /**
     * This saves the result of the renaming.
     * Currently, doesn't work yet.
     * @param actionEvent not sure what this is
     */
    public void save(ActionEvent actionEvent) {
        // TODO
        throw new NotImplementedException("This must be implemented later");
    }

    /**
     * Sets the {@link CardList} that you want to rename
     * @param cardList the {@link CardList} to rename
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }
}
