package client.scenes;

import client.FXConfig;
import client.FXMLInitializer;
import client.utils.CardsUtils;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

import static com.google.inject.Guice.createInjector;

public class CardPopupCtrl {

    private Stage cardPopup;

    private Card card;

    private final CardsUtils cardsUtils;
    private final ServerUtils serverUtils;
    private DeleteCardCtrl deleteCardCtrl;
    private Scene deleteCardScene;
    @FXML
    private Parent root;
    @FXML
    private TextField cardTitle;
    @FXML
    private ChoiceBox<CardList> list;
    @FXML
    private TextArea cardDescription;

    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    /**
     * constructor
     * @param cardsUtils CardsUtils reference
     * @param serverUtils ServerUtils reference
     */
    @Inject
    public CardPopupCtrl(CardsUtils cardsUtils, ServerUtils serverUtils) {
        this.cardsUtils = cardsUtils;
        this.serverUtils = serverUtils;
    }

    /**
     * This initializes the controller.
     * Note that we only create the stage now,
     * because otherwise the root would not be set yet
     * and creating the scene would throw a NullPointerException
     */
    public void initialize() {
        ButtonBar.setButtonData(closeButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonBar.setButtonData(editButton, ButtonBar.ButtonData.RIGHT);
        ButtonBar.setButtonData(deleteButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonBar.setButtonData(saveButton, ButtonBar.ButtonData.APPLY);

        this.cardPopup = new Stage();
        this.cardPopup.initModality(Modality.APPLICATION_MODAL);
        this.cardPopup.setMinWidth(240.0);
        this.cardPopup.setMinHeight(200.0);
        this.cardPopup.setScene(new Scene(root));

        Injector injector = createInjector(new FXConfig());
        FXMLInitializer fxmlInitializer = new FXMLInitializer(injector);
        var deleteCtrl =
                fxmlInitializer.load(DeleteCardCtrl.class, "client", "scenes", "DeleteCard.fxml");
        this.deleteCardCtrl = deleteCtrl.getKey();
        this.deleteCardScene = new Scene(deleteCtrl.getValue());
    }


    /**
     * Sets the card for the controller.
     * @param card the card to attach to the controller
     */
    public void setCard(Card card) {
        this.card = card;
        createView();
    }

    /**
     * Makes the details of the card editable or not
     *
     * @param editable whether the card should be editable
     */
    public void setEditable(boolean editable) {
        this.cardTitle.setEditable(editable);
        this.cardDescription.setEditable(editable);

        this.buttonBar.getButtons().clear();
        if (editable) {
            this.buttonBar.getButtons().addAll(deleteButton, cancelButton, saveButton);
        } else {
            this.buttonBar.getButtons().addAll(closeButton, editButton);
        }
    }

    private void createView() {
        cardTitle.setText(card.getTitle());
        cardsUtils.initializeListsDropDown(list);
        list.getSelectionModel().select((int) card.getListId());
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
//        throw new NotImplementedException("Saving changes hasn't been implemented yet.");
        saveCardChanges();
    }

    /**
     * update card's fields
     * send server request to change a card's details
     */
    public void saveCardChanges() {
        if (cardsUtils.fieldsNotEmpty(cardTitle, list)) {
            try {
                card.setListId(list.getValue().getId());
                card.setTitle(cardTitle.getText());
                serverUtils.editCard(card);
                close();
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        } else {
            cardsUtils.markFields(cardTitle, list);
        }
    }

    /**
     * This function makes the card editable.
     */
    @FXML
    private void edit() {
        setEditable(true);
    }

    /**
     * This function shows the popup.
     * Before calling it, you should call the {@link #setCard(Card)} method.
     */
    public void show() {
        this.cardPopup.show();
    }

    /**
     * Initialize deletion confirmation window and set its scene
     */
    public void showDeleteConfirmation() {
        deleteCardCtrl.initialize(deleteCardScene, card);
    }
}
