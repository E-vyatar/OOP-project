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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardOverviewCtrl implements EventHandler {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;
    private CardPopupCtrl cardPopupCtrl;

    private AddCardCtrl addCardCtrl;
    private Scene addCard;

    private RenameListPopupCtrl renameListPopupCtrl;
    private Stage renameListPopup;

    private List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    @FXML
    private HBox listOfCardLists;

    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup) {
        this.cardPopupCtrl = cardPopup.getKey();

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.renameListPopupCtrl = renameListPopup.getKey();
        this.renameListPopup = new Stage();
        this.renameListPopup.setX(this.renameListPopup.getX() + 100);
        this.renameListPopup.initModality(Modality.APPLICATION_MODAL);
        this.renameListPopup.setScene(new Scene(renameListPopup.getValue(), 300, 200));

        create_cards();
        createButton();
    }

    private void create_cards() {
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

            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(this, new CardList(i, "List " + i, -1), observableList);
            cardListViewCtrlList.add(cardListViewCtrl);
            CardListView cardListView = cardListViewCtrl.getView();
            lists.add(cardListView);
        }

        listOfCardLists.getChildren().addAll(lists);
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
        listOfCardLists.getChildren().add(button);
    }

    /**
     * Adds a new list to the board
     * @param actionEvent
     */
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());
        CardList cardList = CardList.createNewCardList("New List", -1);
        CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(this, cardList, observableList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        listOfCardLists.getChildren().add((listOfCardLists.getChildren().size() - 1), cardListViewCtrl.getView());
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

    /**
     * This function shows a card popup
     * @param card the card to be shown in the popup
     * @param editable whether it should be a popup to edit
     */
    public void showCard(Card card, boolean editable) {
        cardPopupCtrl.setCard(card);
        cardPopupCtrl.setEditable(editable);
        cardPopupCtrl.show();
    }

    /**
     * Open a new window with "AddCard" scene
     */
    public void showAddCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle("Add new Task");
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> {
            addCardCtrl.keyPressed(event);
        });
        addCardCtrl.refresh();
        cardWindow.show();
    }

    public void showRenameList(CardList cardList) {
        renameListPopupCtrl.setCardList(cardList);
        renameListPopup.show();
    }

    public void hideRenameListPopup() {
        renameListPopup.hide();
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