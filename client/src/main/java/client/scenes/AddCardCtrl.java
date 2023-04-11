/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.scenes.service.AddCardService;
import client.utils.CardsUtils;
import com.google.inject.Inject;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddCardCtrl {

    private final AddCardService service;
    private final CardsUtils cardsUtils;

    @FXML
    private TextField title;

    @FXML
    private Button cancel;

    /**
     * constructor
     *
     * @param service the reference to AddCardService
     * @param cardsUtils card utilities reference
     */
    @Inject
    public AddCardCtrl(AddCardService service, CardsUtils cardsUtils) {
        this.service = service;
        this.cardsUtils = cardsUtils;
    }

    /**
     * Initialize the controller
     */
    public void initialize() {
        cardsUtils.limitCharacters(title, 255);
    }

    /**
     * Clear title field and close the "Add new task" window
     */
    public void closeWindow() {
        clearFields();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * send the server a request to add new card
     * and close the window
     */
    public void ok() {
        if (cardsUtils.fieldsNotEmpty(title, null)) {
            try {
                service.createAndSend(title.getText());
                closeWindow();
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            clearFields();
        } else {
            cardsUtils.markFields(title, null);
        }
    }

    /**
     * Clear all data fields for next use
     */
    private void clearFields() {
        title.clear();
    }

    /**
     * Keyboard shortcuts for the buttons
     *
     * @param e keyboard event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER -> ok();
            case ESCAPE -> closeWindow();
            default -> {
            }
        }
    }

    /**
     * Update the lists menu
     */
    public void refresh() {
        clearFields();
    }

    /**
     * Get the CardList for which you're adding a card
     *
     * @return the CardList
     */
    public CardList getCardList() {
        return service.getCardList();
    }

    /**
     * Set the CardList for which you're adding a card
     *
     * @param cardList the cardlist
     */
    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }
}