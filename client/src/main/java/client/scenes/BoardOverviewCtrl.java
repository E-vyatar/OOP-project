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

import client.utils.PollingUtils;
import client.utils.ServerUtils;
import commons.*;
import client.utils.SocketsUtils;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final SocketsUtils socketsUtils;
    private final PollingUtils polling;

    private final MainCtrl mainCtrl;
    private final List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    private CardPopupCtrl cardPopupCtrl;
    private AddCardCtrl addCardCtrl;
    private Scene addCard;
    private DeleteCardCtrl deleteCardCtrl;
    private Scene deleteCard;
    private RenameListPopupCtrl renameListPopupCtrl;
    private Board board;
    @FXML
    private HBox listOfLists;

    /**
     * This constructs BoardOverviewCtrl. BoardOverviewCtrl is the controller
     * linked to the overview of the board.
     * The constructor should not be called manually, since it uses injection.
     * @param server  the ServerUtils of the app - used to load and send data from the server
     * @param sockets socket utils - used to receive changes from the server
     * @param mainCtrl the MainCtrl of the app
     * @param polling  the PollingUtils of the app
    */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl,
                             ServerUtils server,
                             PollingUtils polling,
                             SocketsUtils sockets) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.polling = polling;
        this.socketsUtils = sockets;
    }

    /**
     * Initialises the different popup controllers.
     *
     * @param cardPopup       a pair of the CardPopupCtrl and the root of the to-be scene
     * @param addCard         a pair of the AddCardCtrl and the root of the to-be scene
     * @param renameListPopup a pair of the renameListPopupCtrl and the root of the to-be scene
     * @param deleteCard a pair of the DeleteCardCtrl and the root of the to-be scene
     */
    public void initialize(Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup,
                           Pair<DeleteCardCtrl, Parent> deleteCard) {
        this.cardPopupCtrl = cardPopup.getKey();
        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());
        this.renameListPopupCtrl = renameListPopup.getKey();
        this.deleteCardCtrl = deleteCard.getKey();
        this.deleteCard = new Scene(deleteCard.getValue());

    }

    /**
     * This method should be called when a card has been updated.
     * It will find the card by id and then update it.
     * @param updatedCard the new version of the card
     */
    public void updateCard(Card updatedCard) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            ObservableList<Card> cards = cardListViewCtrl.getObservableCards();
            for (int i = 0; i < cards.size(); i++) {
                // Check if the card has the same id,
                // i.e. if this card is being updated
                if (cards.get(i).getId() == updatedCard.getId()) {
                    // Replace the card
                    cards.set(i, updatedCard);
                    return;
                }
            }
        }
    }

    /**
     * Adds a new list to the board
     */
    @FXML
    private void addList() {
        // Create cardList without specified ID
        CardList cardList = new CardList("New List", board.getId(), board.getCardLists().size());

        try {
            // Add cardList to server and retrieve object with ID
            socketsUtils.send("/app/lists/new", cardList);
            System.out.println("sending the card list message to server" + cardList + "");

        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * when clicking Return to list of boards,
     * you see the list of boards again
     */
    public void returnToBoardList() {
        socketsUtils.unsubscribeAll();
        polling.disconnect();

        mainCtrl.showListOfBoards();
    }


    /**
     * Meant to refresh the board.
     * Currently, it loads the board from the server
     * and builds the UI for it.
     * It also starts the sockets and such.
     *
     * @param boardId the id of the board to be fetched
     */
    public void refresh(long boardId) {

        // Get board with ID = 0
        board = server.getBoard(boardId);

        this.polling.pollForCardUpdates(this);
        socketsUtils.registerMessages("/topic/cards/new", Card.class, newCard -> {
            System.out.println("added new card " + newCard);
            for (CardListViewCtrl cardListViewCtrl : this.cardListViewCtrlList) {
                if (cardListViewCtrl.getCardList().getId() == newCard.getListId()) {
                    // Append new card to the end.
                    cardListViewCtrl.getObservableCards().add(newCard);

                }
            }
        });
        socketsUtils.registerMessages("/topic/cards/delete", Long.class, id ->{
            Card card = getCard(id);
            getCardListViewCtrl(card.getListId()).removeCard(card);
        });
        socketsUtils.registerMessages("/topic/lists/new", CardList.class, newCardList ->{
            CardListViewCtrl cardListViewCtrl = CardListViewCtrl
                .createNewCardListViewCtrl(this, newCardList);
            cardListViewCtrlList.add(cardListViewCtrl);
            listOfLists.getChildren().add(cardListViewCtrl.getCardListNode());
        });
        generateView();
    }

    /**
     * Clears & generates the Board view
     * <p> TODO: Adapt so that listOfLists is ListView instead of HBox (for drag & drop) </p>
     */
    public void generateView() {
        // Deletes all the ListCtrl
        cardListViewCtrlList.clear();

        // Deletes all lists in the View
        listOfLists.getChildren().clear();

        // Loops through the cardLists in the board:
        //      (to add them to the overview)
        for (CardList cardList : board.getCardLists()) {

            // Creates new controller for this list (using creatNewCardListViewCtrl)
            CardListViewCtrl cardListViewCtrl =
                CardListViewCtrl.createNewCardListViewCtrl(
                    this,
                    cardList
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
     * Updates the edited card in the board overview, both title and list
     * @param originalCard The old version of the card
     * @param editedCard The new edited version of the card
     * @param editedListId ID of the new list of the card
     */
    public void updateCard(Card originalCard, Card editedCard, long editedListId) {
        // get original card's list controller
        CardListViewCtrl originalCardListController = getCardListViewCtrl(originalCard.getListId());
        // replace original card by edited version
        originalCardListController.setCard(originalCard.getIdx(), editedCard);

        // get edited card's list controller
        CardListViewCtrl editedCardListController = getCardListViewCtrl(editedListId);
        // check if card's list was changed
        if (originalCardListController != editedCardListController) {
            // move card to edited list
            long indexForEditedCard = editedCardListController.getCardList().getCards().size();
            moveCard(editedCard, editedCardListController.getCardList(),indexForEditedCard);
        }
    }

    /**
     * Opens a new window with "AddCard" scene
     */
    public void showAddCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle("Add new Task to " + addCardCtrl.getCardList().getTitle());
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> addCardCtrl.keyPressed(event));
        addCardCtrl.refresh();
        cardWindow.initModality(Modality.APPLICATION_MODAL);
        cardWindow.show();
    }

    /**
     * Set the cardlist for which you're adding a card
     * In a different method from `showAddCard` because it's easier to pass a parameter
     * @param cardList the cardlist
     */
    public void setCardListForShowAddCard(CardList cardList) {
        addCardCtrl.setCardList(cardList);
    }

    /**
     * Open a new window with "DeleteCard" scene
     * @param card Card to delete
     */
    public void showDeleteCard(Card card) {
        deleteCardCtrl.setCard(card);
        Stage stage = new Stage();
        stage.setTitle("Are you sure you want to delete this card?");
        stage.setScene(deleteCard);
        stage.initModality(Modality.APPLICATION_MODAL);
        deleteCardCtrl.setStage(stage);
        deleteCardCtrl.getStage().show();
    }

    /**
     * Remove card from the board overview
     * @param card Card to remove from the board
     */
    public void removeDeletedCard(Card card) {
        getCardListViewCtrl(card.getListId()).removeCard(card);
    }

    /**
     * Close card pop-up window
     */
    public void closeCardPopUp() {
        cardPopupCtrl.close();
    }

    /**
     * Shows a popup to edit the details (i.e. the title)
     * of a CardList. The popup has an option to rename it.
     *
     * @param controller the controller of the CardList that you can rename.
     */
    public void showRenameList(CardListViewCtrl controller) {
        renameListPopupCtrl.setCardListViewCtrl(controller);
        renameListPopupCtrl.show();
    }

    /**
     * Get a list of current lists in the board
     *
     * @return a list of lists as CardList
     */
    public List<CardList> getAllLists() {
        return cardListViewCtrlList.stream()
            .map(CardListViewCtrl::getCardList)
            .collect(Collectors.toList());
    }

    /**
     * Gets a Card from the board using the Card ID
     *
     * @param id the ID of the Card
     * @return the Card with that ID (or null)
     */
    public Card getCard(long id) {
        for (CardListViewCtrl controller : cardListViewCtrlList) {
            for (Card card : controller.getCards()) {
                if (card.getId() == id) {
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Gets the CardListViewController for the specific CardList ID
     *
     * @param id the id of the CardList
     * @return the CardListViewCtrl for that CardList (or null)
     */
    public CardListViewCtrl getCardListViewCtrl(long id) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            if (cardListViewCtrl.getCardList().getId() == id) {
                return cardListViewCtrl;
            }
        }
        return null;
    }

    /**
     * Moves Card (for drag & drop).
     *
     * @param card     the Card to be moved
     * @param cardList the CardList the card is moved to
     * @param index    the new position index of the card in the CardList
     */
    public void moveCard(Card card, CardList cardList, long index) {

        var oldList = getCardListViewCtrl(card.getListId());
        var newList = getCardListViewCtrl(cardList.getId());

        // TODO: wait for server to confirm move

        if (!server.moveCard(card.getId(), newList.getCardList().getId(),
            index)) {
            return;
        }


        oldList.removeCard(card);
        newList.addCard(card, index);

        // highlight the card
        newList.highlightCard(card);

    }

    /**
     * Move a list from a certain index to a new one.
     *
     * @param listId   the old index
     * @param targetId the new index
     */
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

    /**
     * Getter for ServerUtils of this app
     *
     * @return The ServerUtils for this app
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Deletes a list from the view using the lists Controller
     *
     * @param cardListViewCtrl the controller of the list to delete
     */
    public void deleteList(CardListViewCtrl cardListViewCtrl) {
        cardListViewCtrlList.remove(cardListViewCtrl);
        listOfLists.getChildren().remove(cardListViewCtrl.getCardListNode());
        board.getCardLists().remove(cardListViewCtrl.getCardList());
    }
    /**
     * Getter for the board
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }
}