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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
    private ComboBox<CardList> list;

    @FXML
    private Button cancel;

    @Inject
    public AddCardCtrl(ServerUtils server, CardsUtils cardsUtils, MainCtrl mainCtrl) {
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
     * Close the "Add new task" window
     * TODO
     * Add card to list
     */
    public void ok() {
        if (cardsUtils.fieldsNotEmpty(title, list)) {
            try {
                server.addCard(getCard());
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
            cardsUtils.markFields(title, list);
        }
    }

    /**
     * Create new card object
     *
     * @return new Card, temporarily with dummy data
     */
    private Card getCard() {
        long listSize = server.getCardsByList(list.getValue().getId()).size();
        return new Card(-1, list.getValue().getId(), title.getText(), listSize + 1, list.getValue().getBoardId());
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
        cardsUtils.initializeListsDropDown(list);
    }

    public Stage getCardList() {
        return (Stage) cancel.getScene().getWindow();
    }
}