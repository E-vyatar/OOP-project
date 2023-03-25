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

import client.utils.CardsUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddCardCtrl {

    private final ServerUtils server;
    private final CardsUtils cardsUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    @FXML
    private Button cancel;

    private CardList cardList;

    /**
     * constructor
     * @param server server utilities reference
     * @param cardsUtils card utilities reference
     * @param mainCtrl main controller reference
     */
    @Inject
    public AddCardCtrl(ServerUtils server,CardsUtils cardsUtils , MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardsUtils = cardsUtils;
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
                server.createNewCard(getCard());
                closeWindow();
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            clearFields();
            mainCtrl.showOverview();
        } else {
            cardsUtils.markFields(title, null);
        }
    }

    /**
     * Create new card object
     * @return new Card, temporarily with dummy data
     */
    private Card getCard() {
        long listSize = server.getCardsByList(cardList.getId()).size();
        Card card = new Card(
                -1, cardList.getId(), cardList.getBoardId(), title.getText(),listSize+1);
        return card;
    }

    /**
     * Clear all data fields for next use
     */
    private void clearFields() {
        title.clear();
    }

    /**
     * Keyboard shortcuts for the buttons
     * @param e keyboard event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                closeWindow();
                break;
            default:
                break;
        }
    }

    /**
     * Update the lists menu
     */
    public void refresh() {
        clearFields();
    }

    public CardList getCardList() {
        return cardList;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }
}