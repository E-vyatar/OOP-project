package client.scenes;

import client.scenes.service.RenameListService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class RenameListPopupCtrl {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final RenameListService service;
    private Stage renameListPopup;
    private CardListViewCtrl controller;
    @FXML
    private Parent root;
    @FXML
    private TextField listTitle;

    /**
     * This constructs the controller for the pop-up to rename a list.
     *
     * @param boardOverviewCtrl the BoardOverview
     * @param service the RenameListService
     */
    @Inject
    public RenameListPopupCtrl(BoardOverviewCtrl boardOverviewCtrl,
                               RenameListService service) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.service = service;
    }

    /**
     * This initializes the controller.
     * Note that we only create the stage now,
     * because otherwise the root would not be set yet
     * and creating the scene would throw a NullPointerException
     */
    public void initialize() {
        this.renameListPopup = new Stage();
        this.renameListPopup.setTitle("Edit List");
        this.renameListPopup.initModality(Modality.APPLICATION_MODAL);
        this.renameListPopup.setScene(new Scene(root, 300, 200));
    }

    /**
     * This makes the popup visible.
     * Before calling it, setCardList() should be called.
     */
    public void show() {
        service.setCardList(controller.getCardList());
        listTitle.setText(service.getTitle());
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
            service.save(title);
            close();
        }

    }

    /**
     * Deletes card.
     */
    public void delete() {
        this.boardOverviewCtrl.showDeleteList(
            service.getTitle(),
            service.getListId()
        );
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
