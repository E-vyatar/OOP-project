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
import commons.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BoardOverviewCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    private CardPopupCtrl cardPopupCtrl;
    private AddCardCtrl addCardCtrl;
    private Scene addCard;
    private RenameListPopupCtrl renameListPopupCtrl;
    private Board board;
    @FXML
    private HBox listOfLists;

    /**
     * Constructor
     *
     * @param mainCtrl the MainCtrl of the app
     */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = mainCtrl.getServer();
    }

    /**
     * Initialises the BoardOverview:
     *
     * @param cardPopup the pair of CardPopupCtrl with its root
     * @param addCard the pair of AddCardCtrl with its root
     * @param renameListPopup the pair of RenameListPopupCtrl with its root
     */
    public void initialize(Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup) {
        this.cardPopupCtrl = cardPopup.getKey();
        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());
        this.renameListPopupCtrl = renameListPopup.getKey();
    }

    private void getCardsFromServer() {

    }

    // TODO replace with server call:
    /**
     * Adds a new list to the board
     *
     * @param actionEvent -
     */
    @FXML
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());
        CardList cardList = new CardList("New List", 0, 0);
        CardListViewCtrl cardListViewCtrl = CardListViewCtrl.
            createNewCardListViewCtrl(this, cardList, observableList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstCardId is -1 because it has no cards.
        listOfLists.getChildren()
            .add((listOfLists.getChildren().size()), cardListViewCtrl.getCardListNode());
    }

    /**
     * when clicking Disconnect from Server, the StompSession is ended and scene is set up back to ConnectServerCtrl
     *
     * @param actionEvent -
     */
    public void disconnect(ActionEvent actionEvent) {
        server.getSession().disconnect();
        System.out.println("The client has been disconnected");

        mainCtrl.showConnect();
    }


    /**
     * Meant to refresh the board.
     * Currently, it completely resets the board (deleting all the lists)
     * & rebuilds it (using the board fetched from the server).
     *
     * @param boardId the id of the board to be fetched
     */
    public void refresh(long boardId) {

        // Get board with ID = 0
        board = server.getBoard(boardId);

        // Deletes all the ListCtrl
        cardListViewCtrlList.clear();

        // Deletes all lists in the View
        listOfLists.getChildren().clear();

        // Loops through the cardLists in the board:
        //      (to add them to the overview)
        for(CardList cardList : board.getCardLists()) {

            // Creates an ObservableList for the cards in this list
            ObservableList<Card> observableList = FXCollections.observableList(cardList.getCards());

            // Creates new controller for this list (using creatNewCardListViewCtrl)
            CardListViewCtrl cardListViewCtrl =
                CardListViewCtrl.createNewCardListViewCtrl(
                    this,
                    cardList,
                    observableList // the list of cards (created before)
                );

            // Adds the controller to the controller list
            cardListViewCtrlList.add(cardListViewCtrl);

            // Adds the CardList to the HBox
            listOfLists.getChildren().add(
                (int) cardList.getIdx(),
                cardListViewCtrl.getCardListNode()
            );
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
     * Opens a new window with "AddCard" scene
     */
    public void showAddCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle("Add new Task");
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> addCardCtrl.keyPressed(event));
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
        return cardListViewCtrlList.stream().map(CardListViewCtrl::getCardList)
            .collect(Collectors.toList());
    }

    public Card getCard(long id) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            for (Card card : cardListViewCtrl.getCards()) {
                if (card.getId() == id) {
                    return card;
                }
            }
        }
        return null;
    }

    public CardListViewCtrl getCardListViewCtrl(long id) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            if (cardListViewCtrl.getCardList().getId() == id) {
                return cardListViewCtrl;
            }
        }
        return null;
    }

    public void moveCard(Card card, CardList cardList, long index) {
        System.out.println(
            "Moving card " + card.getId() + " to list " + cardList.getId() + " at index " + index);

        var oldList = getCardListViewCtrl(card.getListId());
        var newList = getCardListViewCtrl(cardList.getId());
        // TODO: wait for server to confirm move
        oldList.removeCard(card);
        newList.addCard(card, index);

        // highlight the card
        newList.highlightCard(card);

    }

    public void moveList(long listId, long targetId) {
        var list = getCardListViewCtrl(listId);
        var target = getCardListViewCtrl(targetId);
        int index = cardListViewCtrlList.indexOf(target);
        var view = list.getCardListNode();
        // TODO: wait for server to confirm move

        listOfLists.getChildren().remove(view);
        listOfLists.getChildren().add(index, view);
        cardListViewCtrlList.remove(list);
        cardListViewCtrlList.add(index, list);

    }
}