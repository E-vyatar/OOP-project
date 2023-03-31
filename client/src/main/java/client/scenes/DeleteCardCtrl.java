package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class DeleteCardCtrl {

    private final ServerUtils serverUtils;
    private final BoardOverviewCtrl boardOverviewCtrl;

    private Stage stage;
    private Card card;

    /**
     * Constructor
     *
     * @param serverUtils       Reference to ServerUtils
     * @param boardOverviewCtrl Reference to BoardOverviewCtrl
     */
    @Inject
    public DeleteCardCtrl(ServerUtils serverUtils, BoardOverviewCtrl boardOverviewCtrl) {
        this.serverUtils = serverUtils;
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    /**
     * Close the window
     */
    public void closeConfirmation() {
        stage.hide();
    }

    /**
     * Send Delete request to the server of the chosen card
     * and close the window.
     * Raise an alert if card wasn't deleted from database
     */
    public void deleteCard() {
        try {
            boolean wasDeleted = serverUtils.deleteCard(card);
            if (wasDeleted) {
                boardOverviewCtrl.removeDeletedCard(card);
                closeConfirmation();
                boardOverviewCtrl.closeCardPopUp();
            } else {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Card wasn't deleted, please try again");
                alert.showAndWait();
            }
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Setter for card
     * @param card Card to set card to
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Getter for stage
     * @return Stage of this window
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Setter for stage
     * @param stage the Stage to set stage to
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
