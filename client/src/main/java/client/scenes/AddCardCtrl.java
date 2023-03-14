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

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AddCardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private TextField title;

    @FXML
    private ChoiceBox list;

    @FXML
    private Button cancel;

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl, BoardOverviewCtrl boardOverviewCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    /**
     * Clear title field and close the "Add new task" window
     */
    public void cancel() {
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
        try {
//            server.addCard(getCard());
            String chosenList = (String) list.getValue();
//            boardOverviewCtrl.addTaskToList(chosenList, getCard());
            this.cancel();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Create new card object
     * @return new Card, temporarily with dummy data
     */
    private Card getCard() {
        return new Card("25", "36", title.getText(), "47", "90");
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
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Update the lists menu
     */
    public void refresh() {
        list.getItems().clear();
        List<String> listsNames = boardOverviewCtrl.getListsNames();
        for (String name : listsNames) {
            list.getItems().add(name);
        }
    }
}