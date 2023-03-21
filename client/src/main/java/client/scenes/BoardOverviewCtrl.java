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

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;
    private List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    @FXML
    private HBox listOfLists;

    /**
     * This constructs BoardOverviewCtrl. BoardOverviewCtrl is the controller
     * linked to the overview of the board.
     * The constructor should not be called manually, since it uses injection.
     * @param utils
     * @param mainCtrl
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the BoardOverviewCtrl.
     * @param location  this is unused
     * @param resources this is unused
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCards();
    }

    /**
     * This method creates the hardcoded cards.
     */
    private void createCards() {
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
            ObservableList<Card> observableList = FXCollections.observableList(cards);

            CardList cardList = new CardList(i, "List " + i, -1);

            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(
                    mainCtrl, this, cardList, observableList);
            cardListViewCtrlList.add(cardListViewCtrl);
            CardListView cardListView = cardListViewCtrl.getView();
            lists.add(cardListView);
        }

        listOfLists.getChildren().addAll(lists);

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
        listOfLists.setAlignment(Pos.CENTER_RIGHT);
        listOfLists.getChildren().add(button);
    }

    /**
     * Adds a new list to the board
     * @param actionEvent
     */
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());
        CardList cardList = CardList.createNewCardList("New List", -1);
        CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(
                mainCtrl, this, cardList, observableList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        int indexSecondToLast = listOfLists.getChildren().size() - 1;
        listOfLists.getChildren().add(indexSecondToLast, cardListViewCtrl.getView());
    }

    /**
     * Refreshes the boardOverview. Currently empty (should be removed?)
     */
    public void refresh() {

    }

    /**
     * This method unselects all cards except the cards in the given list.
     * @param exclude The CardListViewCtrl for which to not unselect cards.
     */
    public void unselectCards(CardListViewCtrl exclude) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            if (cardListViewCtrl == exclude) {
                continue;
            }
            cardListViewCtrl.clearSelection();
        }
    }

    /**
     * Show pop-up to create a new card.
     */
    public void openNewTaskWindow() {
        mainCtrl.showAddCard();
    }

    /**
     * Get a list of current lists in the board
     * @return a list of lists as CardList
     */
    public List<CardList> getAllLists() {
        return cardListViewCtrlList
                .stream()
                .map(CardListViewCtrl::getCardList)
                .collect(Collectors.toList());
    }
}