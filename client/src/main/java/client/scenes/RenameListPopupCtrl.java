package client.scenes;

import client.utils.ServerUtils;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;

public class RenameListPopupCtrl {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private Stage renameListPopup;
    private CardList cardList;


    @FXML
    private Parent root;
    @FXML
    private TextField listTitle;

    @Inject
    public RenameListPopupCtrl(ServerUtils utils, BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
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
     *
     * @param actionEvent the event that triggers the call of this function
     */
    public void close(ActionEvent actionEvent) {
        this.renameListPopup.hide();
    }

    public void save(ActionEvent actionEvent) {
        // TODO
        throw new NotImplementedException("This must be implemented later");
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }
}
