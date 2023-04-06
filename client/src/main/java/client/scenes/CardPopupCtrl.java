package client.scenes;

import client.utils.CardsUtils;
import client.utils.ServerUtils;
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
import java.util.Optional;

public class CardPopupCtrl {

    private Card card;
    private final CardsUtils cardsUtils;
    private final ServerUtils server;
    private Stage cardPopup;
    private final BoardOverviewCtrl boardOverviewCtrl;
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
     *
     * @param cardsUtils CardsUtils reference
     * @param server     ServerUtils reference
     * @param boardOverviewCtrl BoardOverviewCtrl reference
     */
    @Inject
    public CardPopupCtrl(CardsUtils cardsUtils, ServerUtils server,
                         BoardOverviewCtrl boardOverviewCtrl) {
        this.cardsUtils = cardsUtils;
        this.server = server;
        this.boardOverviewCtrl = boardOverviewCtrl;
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
    }


    /**
     * Sets the card for the controller.
     *
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

    /**
     * Insert card's values in the fields of the pop-up
     */
    private void createView() {
        cardTitle.setText(card.getTitle());
        cardsUtils.initializeListsDropDown(list);
        Optional<CardList> chosenList = boardOverviewCtrl.getAllLists().stream()
                .filter(currentList -> currentList.getId() == card.getListId())
                .findFirst();
        chosenList.ifPresent(cardList -> list.getSelectionModel().select(cardList));
        cardDescription.setText("Here there will be a description.");
    }

    /**
     * This function closes the popup.
     * It is called by pressing the close button in the popup.
     */
    @FXML
    public void close() {
        this.cardPopup.hide();
    }

    /**
     * Update card's fields and send server request to change a card's details.
     * The card's list index is updated in a different server request,
     * triggered by a method in BoardOverviewCtrl
     */
    @FXML
    public void saveCardChanges() {
        if (cardsUtils.fieldsNotEmpty(cardTitle, list)) {
            try {
                // We create a new card with the updated information
                // we send this to the server
                // Then we will receive this card via sockets/long-polling and update it
                // in the UI.
                long cardId = card.getId();
                long listId = list.getValue().getId();
                long boardId = card.getBoardId();
                long idx = card.getIdx();
                String title = cardTitle.getText();
                Card updatedCard = new Card(cardId, listId, boardId, title, idx);
                if (listId == card.getListId()) {
                    server.editCard(updatedCard);
                }
                else {
                    Card cardFromDatabase =  server.editCard(updatedCard);
                    long newIndex = list.getValue().getCards().size();
                    boardOverviewCtrl.requestMoveCard(cardFromDatabase, list.getValue(), newIndex);
                }
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
        boardOverviewCtrl.showDeleteCard(card);
    }
}
