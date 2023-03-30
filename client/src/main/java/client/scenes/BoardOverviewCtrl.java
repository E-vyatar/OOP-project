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
     * This constructs BoardOverviewCtrl. BoardOverviewCtrl is the controller
     * linked to the overview of the board.
     * The constructor should not be called manually, since it uses injection.
     * @param mainCtrl the MainCtrl of the app
     * @param server the ServerUtils of the app
     */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initialises the different popup controllers.
     *
     * @param cardPopup a pair of the CardPopupCtrl and the root of the to-be scene
     * @param addCard a pair of the AddCardCtrl and the root of the to-be scene
     * @param renameListPopup a pair of the renameListPopupCtrl and the root of the to-be scene
     */
    public void initialize(Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup) {
        this.cardPopupCtrl = cardPopup.getKey();
        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());
        this.renameListPopupCtrl = renameListPopup.getKey();
    }

    /**
     * Adds a new list to the board
     *
     * @param actionEvent -
     */
    @FXML
    private void addList(ActionEvent actionEvent) {
        CardList cardList = new CardList("New List", 0, 0);
        CardListViewCtrl cardListViewCtrl = CardListViewCtrl.createNewCardListViewCtrl(
                this, cardList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        int numLists = listOfLists.getChildren().size();
        listOfLists.getChildren().add(numLists, cardListViewCtrl.getCardListNode());
    }

    /**
     * When clicking Disconnect from Server, the StompSession is ended
     * and scene is set up back to ConnectServerCtrl
     *
     * @param actionEvent unused
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
        for(CardList cardList : board.getCardLists()) {

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

    /**
     * Sets the CardList for which you're adding a card
     *
     * @param cardList the cardlist
     */
    public void setCardListForShowAddCard(CardList cardList) {
        addCardCtrl.setCardList(cardList);
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
        for (CardList cardList : board.getCardLists()) {
            for (Card card : cardList.getCards()) {
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
     * @param card the Card to be moved
     * @param cardList the CardList the card is moved to
     * @param index the new position index of the card in the CardList
     */
    public void moveCard(Card card, CardList cardList, long index) {
        System.out.println("Moving card " + card.getId() +
                " to list " + cardList.getId() + " at index " + index);

        var oldList = getCardListViewCtrl(card.getListId());
        var newList = getCardListViewCtrl(cardList.getId());
        // TODO: wait for server to confirm move
        oldList.removeCard(card);
        newList.addCard(card, index);

        // highlight the card
        newList.highlightCard(card);

    }

    /**
     * Move a list from a certain index to a new one.
     *
     * @param listId the old index
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
}