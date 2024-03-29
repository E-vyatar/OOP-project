package client.scenes;

import client.utils.SocketsUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class DeleteCardCtrl {

    private final BoardOverviewCtrl boardOverviewCtrl;
    private final SocketsUtils socketUtils;

    private Stage stage;
    private Card card;

    /**
     * Constructor
     *
     * @param socketsUtils      Reference to ServerSockets
     * @param boardOverviewCtrl Reference to BoardOverviewCtrl
     */
    @Inject
    public DeleteCardCtrl(SocketsUtils socketsUtils,
                          BoardOverviewCtrl boardOverviewCtrl) {
        this.socketUtils = socketsUtils;
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
            socketUtils.send("/app/cards/delete", card.getId());
            System.out.println("message been sent to delete with" + card.toString());
            closeConfirmation();
            boardOverviewCtrl.closeCardPopUp();
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
