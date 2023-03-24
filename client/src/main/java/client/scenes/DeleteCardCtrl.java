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

    private final CardPopupCtrl cardPopupCtrl;
    private final ServerUtils serverUtils;

    private Stage stage;

    private Card card;

    @FXML
    private Parent root;

    @Inject
    public DeleteCardCtrl(CardPopupCtrl cardPopupCtrl, ServerUtils serverUtils) {
        this.cardPopupCtrl = cardPopupCtrl;
        this.serverUtils = serverUtils;
    }

    public void initialize() {
        stage = new Stage();
        stage.setTitle("Are you sure you want to delete this card?");
        stage.setScene(new Scene(root));
        this.card = cardPopupCtrl.card;
        stage.show();
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void closeConfirmation() {
        stage.hide();
    }

    public void deleteCard() {
        try {
            serverUtils.deleteCard(card);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        finally {
            closeConfirmation();
            cardPopupCtrl.close();
        }
    }
}
