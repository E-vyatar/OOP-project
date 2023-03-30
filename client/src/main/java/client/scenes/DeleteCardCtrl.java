package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class DeleteCardCtrl {

    private final ServerUtils serverUtils;

    private Stage stage;

    private Card card;

    @FXML
    private Parent root;

    /**
     * Create an instance of DeleteCardCtrl
     *
     * @param serverUtils the class containing the methods to communicate with the server
     */
    @Inject
    public DeleteCardCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * This iniatializes the scene
     */
    public void initialize() {
        stage = new Stage();
        stage.setTitle("Are you sure you want to delete this card?");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This sets the card to be deleted.
     * This must be called before it becomes visibile.
     *
     * @param card card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * This closes the popup.
     */
    public void closeConfirmation() {
        stage.hide();
    }

    /**
     * This deletes the attached card.
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
