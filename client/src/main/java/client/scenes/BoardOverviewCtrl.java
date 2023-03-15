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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
        create_cards();
    }

    private void create_cards() {
        /*
            Currently, this method just creates arbitrary data.
            This data doesn't properly use the format as it's stored in the DB.
            When linking with the server side,
            the cards in a list should be converted into an ObservableList.
         */
        var lists = new ArrayList();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cards.add(new Card(i * 4 + j, i, "Card " + i + "." + j, -1 , -1));
            }
        }

        ObservableList<Card> observableList = FXCollections.observableList(cards);

        for (int i = 0; i < 4; i++) {
            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(mainCtrl, new CardList(i, "List " + i, -1), observableList);
            CardListView cardListView = cardListViewCtrl.getView();
            lists.add(cardListView);
        }

        list_of_lists.getChildren().addAll(lists);

        createButton();
    }

    /**
     * Creates a button to add a new list to the board
     */
    private void createButton() {
        // Create button and add to list_of_lists
        Button button = new Button("Add list");
        button.setOnAction(this::addList);
            //set button margin
        HBox.setMargin(button, new javafx.geometry.Insets(0, 0, 0, 25));
        list_of_lists.setAlignment(Pos.CENTER_RIGHT);
        list_of_lists.getChildren().add(button);
    }

    /**
     * Adds a new list to the board
     * @param actionEvent
     */
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());
        CardList cardList = CardList.createNewCardList("New List", -1);
        CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(mainCtrl, cardList, observableList);

        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        list_of_lists.getChildren().add((list_of_lists.getChildren().size() - 1), new CardListView(mainCtrl, cardList, cardListViewCtrl, observableList));
    }

    public void refresh() {

    }

    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        System.out.println("Source: " + source);
        if (source instanceof CardPopupCtrl) {
            CardPopupCtrl card = (CardPopupCtrl) source;
        }
    }

    public void openNewTaskWindow() {
        mainCtrl.showAddCard();
    }

    /**
     * Get the names of current lists in the board
     * @return a list of lists names as strings
     */
    public List<String> getListsNames() {
        return list_of_lists.getChildren()
                .stream()
                .map(node -> (CardListView) node)
                .map(CardListView::getListName)
                .collect(Collectors.toList());
    }
}