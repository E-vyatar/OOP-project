package client.scenes;

import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class RenameListPopupCtrl {

    private final ServerUtils server;
    private Stage renameListPopup;
    private CardListViewCtrl controller;
    @FXML
    private Parent root;
    @FXML
    private TextField listTitle;

    /**
     * This constructs the controller for the pop-up to rename a list.
     *
     * @param server the SeverUtils
     */
    @Inject
    public RenameListPopupCtrl(ServerUtils server) {
        this.server = server;
    }

    /**
     * This initializes the controller.
     * Note that we only create the stage now,
     * because otherwise the root would not be set yet
     * and creating the scene would throw a NullPointerException
     */
    public void initialize() {
        this.renameListPopup = new Stage();
        this.renameListPopup.setX(this.renameListPopup.getX() + 100);
        this.renameListPopup.initModality(Modality.APPLICATION_MODAL);
        this.renameListPopup.setScene(new Scene(root, 300, 200));
    }

    /**
     * This makes the popup visible.
     * Before calling it, setCardList() should be called.
     */
    public void show() {
        this.renameListPopup.show();
    }

    /**
     * This closes the popup.
     * Currently, the popup is closed irrespective of the parameter passed.
     */
    public void close() {
        this.renameListPopup.hide();
    }

    /**
     * This saves the result of the renaming.
     * Currently, doesn't work yet.
     *
     */
    public void save() {
        String title = listTitle.getText();
        listTitle.setStyle("-fx-border-color: inherit");
        if (title.isEmpty()) {
            listTitle.setStyle("-fx-border-color: red");
        } else {
            CardList temp = controller.getCardList();
            temp.setTitle(title);
            temp = server.editCardList(temp);
            controller.setCardList(temp);
            controller.resetTitle();
            close();
        }

    }

    /**
     * Sets the {@link CardListViewCtrl} of the CardList that you want to rename.
     *
     * @param controller the {@link CardListViewCtrl} of the CardList to rename
     */
    public void setCardListViewCtrl(CardListViewCtrl controller) {
        this.controller = controller;
    }
}
