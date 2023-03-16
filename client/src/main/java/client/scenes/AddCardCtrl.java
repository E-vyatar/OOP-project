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
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.List;

public class AddCardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private TextField title;

    @FXML
    private ComboBox<CardList> list;

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
        if (!emptyFields()) {
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
            markFields();
        }
    }

    private boolean emptyFields() {
        return title.getText().isBlank() || list.getSelectionModel().isEmpty();
    }

    private void markFields() {
        if (title.getText().isBlank()) {
            title.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        if (list.getSelectionModel().isEmpty()) {
            list.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            title.setStyle("");
            list.setStyle("");
        }));
        timeline.play();
    }

    /**
     * Create new card object
     * @return new Card, temporarily with dummy data
     */
    private Card getCard() {
        long listSize = server.getCardsByList(list.getValue().getId()).size();
        Card card = new Card(list.getValue().getId(), title.getText(), listSize+1, list.getValue().getBoardId());
        return card;
    }

    /**
     * Clear all data fields for next use
     */
    private void clearFields() {
        title.clear();
        list.getItems().clear();
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
        List<CardList> cardsLists = boardOverviewCtrl.getAllLists();
        list.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(CardList item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
        list.setConverter(new StringConverter<>() {
            @Override
            public String toString(CardList cardList) {
                if (cardList != null) {
                    return cardList.getTitle();
                } else {
                    return "";
                }
            }

            @Override
            public CardList fromString(String string) {
                // not used, but must be implemented
                return null;
            }
        });
        list.getItems().addAll(cardsLists);
    }

    public ComboBox<CardList> getList() {
        return list;
    }
}