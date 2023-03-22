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
import commons.Board;
import commons.CardList;
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
    private List<CardListViewCtrl> cardListViewCtrlList;
    @FXML
    private HBox listOfLists;
    private Board board;

    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: currently the id is hardcoded, this should be changed when multiboard feature gets added.
        this.board = utils.getBoard(0);
        createCardLists();
        createButton();
    }

    /**
     * This method creates the CardListViews based on the data in the board.
     * {@link this.cardListViewCtrlList} will be initialized, and the views
     * will be added to the scene.
     */
    private void createCardLists() {
        this.cardListViewCtrlList  = new ArrayList<>();
        var HBoxChildren = this.listOfLists.getChildren();
        for (CardList cardList : this.board.getCardLists()) {
            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(this.mainCtrl, this, cardList);
            cardListViewCtrlList.add(cardListViewCtrl);
            HBoxChildren.add(cardListViewCtrl.getView());
        }
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
        CardList cardList = CardList.createNewCardList("New List", -1);
        CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(mainCtrl, this, cardList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        listOfLists.getChildren().add((listOfLists.getChildren().size() - 1), cardListViewCtrl.getView());
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