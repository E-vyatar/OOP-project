package client.scenes;

import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.NotImplementedException;

public class CardPopupCtrl {

    private Stage cardPopup;

    public Card card;

    @FXML
    private Parent root;
    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;

    /**
     * This initializes the controller.
     * Note that we only create the stage now,
     * because otherwise the root would not be set yet
     * and creating the scene would throw a NullPointerException
     */
    public void initialize() {
        this.cardPopup = new Stage();
        this.cardPopup.initModality(Modality.WINDOW_MODAL);
        this.cardPopup.setMinWidth(240.0);
        this.cardPopup.setMinHeight(200.0);
        this.cardPopup.setScene(new Scene(root));
    }


    public void setCard(Card card) {
        this.card = card;
        createView();
    }

    private void createView() {
        cardTitle.setText(card.getTitle());

        cardDescription.setText("Here there will be a description.");

    }

    /**
     * This function closes the popup.
     * It is called by pressing the close button in the popup.
     */
    @FXML
    private void close() {
        this.cardPopup.hide();
    }

    @FXML
    private void save() {
        // TODO: allow to save data when editing card
        throw new NotImplementedException("Saving changes hasn't been implemented yet.");
    }

    /**
     * This function shows the popup.
     * Before calling it, you should call the {@link #setCard(Card)} method.
     */
    public void show(){
        this.cardPopup.show();
    }
}
