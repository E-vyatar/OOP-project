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
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    @FXML
    private HBox listOfLists;
    private Board board;

    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This method creates the CardListViews based on the data in the board.
     * {@link this.cardListViewCtrlList} will be initialized, and the views
     * will be added to the scene.
     */
    private void createCardLists() {
        this.cardListViewCtrlList = new ArrayList<>();
        var HBoxChildren = this.listOfLists.getChildren();
        for (CardList cardList : this.board.getCardLists()) {
            // Right now, we're creating an observable list here,
            // we should investigate whether it can be in commons,
            // So everything can be dynamic automatically.
            ObservableList<Card> observableList = FXCollections.observableList(cardList.getCards());
            CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(this, cardList, observableList);
            cardListViewCtrlList.add(cardListViewCtrl);
            HBoxChildren.add(cardListViewCtrl.getView());
        }
    }

    public void initialize(Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup) {
        this.cardPopupCtrl = cardPopup.getKey();

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.renameListPopupCtrl = renameListPopup.getKey();


        // This should be moved in the multi-board feature
        this.board = utils.getBoard(0);
        createCardLists();
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
     *
     * @param actionEvent
     */
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());

        // Create a new list on the server
        //TODO get board id from somewhere, currently hardcoded and not working on server/db side
        CardList cardList = createNewCardList("New List", 1, list_of_lists.getChildren().size());
        if (cardList == null) {
            return;
        }

        CardList cardList = CardList.createNewCardList("New List", -1);

        board.getCardLists().add(cardList);
        // TODO: communicate with server and integrate this with the board
        ObservableList<Card> observableList = FXCollections.observableList(cardList.getCards());
        CardListViewCtrl cardListViewCtrl = new CardListViewCtrl(this, cardList, observableList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        listOfLists.getChildren().add((listOfLists.getChildren().size() - 1), cardListViewCtrl.getView());
    }

    /**
     * Creates a new list on the server and returns the list.
     *
     * @param cardListTitle The title of the list
     * @param boardId       The id of the board the list is on
     * @return The list that was created and succesfully sent to the server
     */
    private CardList createNewCardList(String cardListTitle, long boardId, long idx) {

        // Create the actual list with id 1
        // TODO: get id from somewhere. Id=-1 does not work on server/db side because of ID>=0 constraint.
        long cardListId = 1;
        CardList cardList = new CardList(cardListId, cardListTitle, idx, boardId);

        // Send the list to the server
        // Show error if the server returns an error, return null
        try {
            utils.addList(cardList);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }

        return cardList;
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
     *
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
     *
     * @param card     the card to be shown in the popup
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
        cardWindow.setTitle("Add new Task to " + addCardCtrl.getCardList().getTitle());
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> {
            addCardCtrl.keyPressed(event);
        });
        addCardCtrl.refresh();
        cardWindow.show();
    }

    public void setCardListForShowAddCard(CardList cardList) {
        addCardCtrl.setCardList(cardList);
    }

    /**
     * Shows a popup to edit the details (i.e. the title)
     * of a CardList. The popup has an option to rename it.
     *
     * @param cardList the CardList that you can rename.
     */
    public void showRenameList(CardList cardList) {
        renameListPopupCtrl.setCardList(cardList);
        renameListPopupCtrl.show();
    }

    /**
     * Get a list of current lists in the board
     *
     * @return a list of lists as CardList
     */
    public List<CardList> getAllLists() {
        return cardListViewCtrlList
                .stream()
                .map(CardListViewCtrl::getCardList)
                .collect(Collectors.toList());
    }
}