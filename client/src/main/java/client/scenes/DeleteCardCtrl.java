package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class DeleteCardCtrl {

    private final ServerUtils serverUtils;

    private Stage stage;
    private Card card;

    /**
     * Constructor
     * @param serverUtils Reference to ServerUtils
     */
    @Inject
    public DeleteCardCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Initialize the deletion confirmation window
     * @param scene Scene to present in the stage
     * @param card The Card to delete
     */
    public void initialize(Scene scene, Card card) {
        this.card = card;
        this.stage = new Stage();
        stage.setTitle("Are you sure you want to delete this card?");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Close the window
     */
    public void closeConfirmation() {
        stage.hide();
    }

    /**
     * Send Delete request to the server of the chosen card
     * and close the window
     */
    public void deleteCard() {
        try {
            serverUtils.deleteCard(card);
            closeConfirmation();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
