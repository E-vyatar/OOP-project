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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardOverviewCtrl implements EventHandler {

    private final ServerUtils utils;
    private final MainCtrl mainCtrl;
    private final List<CardListViewCtrl> cardListViewCtrlList = new ArrayList<>();
    private CardPopupCtrl cardPopupCtrl;
    private AddCardCtrl addCardCtrl;
    private Scene addCard;
    private RenameListPopupCtrl renameListPopupCtrl;
    @FXML
    private HBox listOfLists;

    /**
     * This constructs BoardOverviewCtrl. BoardOverviewCtrl is the controller
     * linked to the overview of the board.
     * The constructor should not be called manually, since it uses injection.
     * @param utils class containing the methods to load data from the server
     * @param mainCtrl the main controller
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils utils, MainCtrl mainCtrl) {
        this.utils = utils;
        this.mainCtrl = mainCtrl;
//        if (!this.utils.isConnectionAlive()) showConnect();
    }
    /**
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

        createCards();
    }

    /**
     * Refresh the board. Right now it doesn't do anything,
     * but when we have the multi-board feature this is necessary.
     */
    public void refresh() {

    }

    /**
     * This method creates the hardcoded cards.
     */
    @Deprecated
    private void createCards() {
        /*
            Currently, this method just creates arbitrary data.
            This data doesn't properly use the format as it's stored in the DB.
            When linking with the server side,
            the cards in a list should be converted into an ObservableList.
         */

        ArrayList<AnchorPane> lists = new ArrayList<>();
        // Create four lists
        for (long i = 0; i < 4; i++) {
            List<Card> cards = new ArrayList<>();
            for (long j = 0; j < 4; j++) {
                cards.add(new Card(i * 4 + j, i, "Card " + i + "." + j, j, -1));
            }
            ObservableList<Card> observableList = FXCollections.observableList(cards);

            CardList cardList = new CardList("List " + i, 0, 0);
            CardListViewCtrl cardListViewCtrl = CardListViewCtrl.createNewCardListViewCtrl(
                    this, cardList, observableList);
            cardListViewCtrlList.add(cardListViewCtrl);
            lists.add(cardListViewCtrl.getCardListNode());
        }

        listOfLists.getChildren().addAll(lists);
    }

    private void getCardsFromServer() {

    }

    /**
     * Adds a new list to the board
     *
     * @param actionEvent
     */
    @FXML
    private void addList(ActionEvent actionEvent) {
        // Create a new list where cards can be added to
        ObservableList<Card> observableList = FXCollections.observableList(new ArrayList<>());

        CardList cardList = new CardList("New List", 0, 0);
        CardListViewCtrl cardListViewCtrl = CardListViewCtrl.createNewCardListViewCtrl(
                this, cardList, observableList);
        cardListViewCtrlList.add(cardListViewCtrl);
        // Add a new list to the list of lists. The firstcardId is -1 because it has no cards.
        int numLists = listOfLists.getChildren().size();
        listOfLists.getChildren().add(numLists, cardListViewCtrl.getCardListNode());
    }

    /**
     * when clicking Disconnect from Server, the Stompsession is ended
     * and scene is set up back to ConnectServerCtrl
     *
     * @param actionEvent unused
     */
    public void disconnect(ActionEvent actionEvent) {
        utils.getSession().disconnect();
        System.out.println("The client has been disconnected");

        mainCtrl.showConnect();
    }

    @Override
    @SuppressWarnings("MissingJavadocMethod")
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
        cardWindow.setTitle("Add new Task");
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> addCardCtrl.keyPressed(event));
        addCardCtrl.refresh();
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
     * Add card to the list view. addCard is called with negative index
     * to put the card at the end of the list
     * @param cardList The list to add the card to
     * @param card The card to add to the list
     */
    public void addCardToBoardOverview(CardList cardList, Card card) {
        int idx = getAllLists().indexOf(cardList);
        cardListViewCtrlList.get(idx).addCard(card, -1);
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
        return cardListViewCtrlList.stream()
                .map(CardListViewCtrl::getCardList)
                .collect(Collectors.toList());
    }
    @SuppressWarnings("MissingJavadocMethod")
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
    @SuppressWarnings("MissingJavadocMethod")
    public CardListViewCtrl getCardListViewCtrl(long id) {
        for (CardListViewCtrl cardListViewCtrl : cardListViewCtrlList) {
            if (cardListViewCtrl.getCardList().getId() == id) {
                return cardListViewCtrl;
            }
        }
        return null;
    }
    @SuppressWarnings("MissingJavadocMethod")
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
     * Move a list from a certain index to a new one
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
}
