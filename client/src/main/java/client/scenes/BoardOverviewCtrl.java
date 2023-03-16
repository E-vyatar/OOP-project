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
import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable, EventHandler {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox list_of_lists;

    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void refresh() {
        /*
            Currently, this method just creates arbitrary data.
            This data doesn't properly use the format as it's stored in the DB.
            When linking with the server side,
            the cards in a list should be converted into an ObservableList.
         */
        var lists = new ArrayList();
        // Create four lists
        for (long i = 0; i < 4; i++) {
            List<Card> cards = new ArrayList<>();
            for (long j = 0; j < 4; j++) {
                cards.add(new Card(i * 4 + j, i, "Card " + i + "." + j, j , -1));
            }
            ObservableList observableList = FXCollections.observableList(cards);

            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(mainCtrl, new CardList(i, "List " + i, -1), observableList);
            CardListView cardListView = cardListViewCtrl.getView();
            lists.add(cardListView);
        }

        list_of_lists.getChildren().addAll(lists);
    }

    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        System.out.println("Source: " + source);
        if (source instanceof CardPopupCtrl) {
            CardPopupCtrl card = (CardPopupCtrl) source;
        }
    }
}