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
import client.utils.SocketsUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddCardCtrl {

    private final ServerUtils server;
    private final SocketsUtils socketsUtils;
    private final CardsUtils cardsUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    @FXML
    private Button cancel;

    private CardList cardList;

    /**
     * constructor
     *
     * @param server     server utilities reference
     * @param cardsUtils card utilities reference
     * @param mainCtrl   main controller reference
     */
    @Inject
    public AddCardCtrl(ServerUtils server, SocketsUtils socketUtils, CardsUtils cardsUtils, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardsUtils = cardsUtils;
        this.socketsUtils = socketUtils;
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
                socketsUtils.send("/app/cards/new", getCard());
//                server.addCard(getCard());
                System.out.println("add a card");
                closeWindow();
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            clearFields();
            mainCtrl.showOverview(0);
        } else {
            cardsUtils.markFields(title, null);
        }
    }

    /**
     * Create new card object
     *
     * @return new Card, temporarily with dummy data
     */
    private Card getCard() {
        long listSize = server.getCardsByList(cardList.getId()).size();
        return new Card(
                -1, cardList.getId(), title.getText(), listSize + 1, cardList.getBoardId());
    }

    /**
     * Clear all data fields for next use
     */
    private void clearFields() {
        title.clear();
//        list.getItems().clear();
    }

    /**
     * Keyboard shortcuts for the buttons
     *
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

    /**
     * Get the CardList for which you're adding a card
     * @return the CardList
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * Set the CardList for which you're adding a card
     * @param cardList the cardlist
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }
}