package client.scenes;

import client.scenes.service.DeleteListService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Used to control the 'delete list confirmation' popup.
 */
public class DeleteListPopupCtrl {

    private final RenameListPopupCtrl renameListPopupCtrl;

    /**
     * The service which contains testable methods of this controller.
     */
    private final DeleteListService service;
    private Stage stage;
    @FXML
    private Label listTitleLabel;
    @FXML
    private Parent root;

    /**
     * Constructor
     *
     * @param renameListPopupCtrl the RenameListPopupCtrl
     * @param service the DeleteListService
     */
    @Inject
    public DeleteListPopupCtrl(RenameListPopupCtrl renameListPopupCtrl,
                               DeleteListService service) {
        this.renameListPopupCtrl = renameListPopupCtrl;
        this.service = service;
    }

    /**
     * This initializes the controller.
     * Note that we only create the stage now,
     * because otherwise the root would not be set yet
     * and creating the scene would throw a NullPointerException
     */
    public void initialize() {
        this.stage = new Stage();
        this.stage.setTitle("Delete Confirmation");
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.setScene(new Scene(root));
    }

    /**
     * Shows the 'delete list confirmation' popup & sets the label of the popup
     *
     * @param listTitle the title of the list to be deleted
     * @param listId the id of the list to be deleted
     */
    public void show(String listTitle, long listId) {
        listTitleLabel.setText(DeleteListService.generateLabelText(listTitle));
        service.setListId(listId);
        this.stage.show();
    }

    /**
     * Called when 'delete' button is pressed.
     * Sends delete request to websocket and closes the popups.
     */
    public void delete() {
        service.delete();
        close();
        this.renameListPopupCtrl.close();
    }

    /**
     * Called when 'cancel' button is pressed.
     * Closes the confirmation popup.
     */
    public void close() {
        this.stage.hide();
    }
}
