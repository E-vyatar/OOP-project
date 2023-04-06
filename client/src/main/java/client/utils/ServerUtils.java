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
package client.utils;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.messages.MoveCardMessage;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {


    private String server;
    private String password;

    /**
     * Set the hostname of the server.
     * Use this method when the user is not an admin
     *
     * @param hostname the hostname
     */
    public void setHostname(String hostname) {
        this.server = "http://" + hostname + ":8080";
        this.password = null;
    }

    /**
     * Set the hostname of the server and the password of the admin.
     * Use this method only when the user is an admin.
     *
     * @param hostname the hostname
     * @param password the admin password
     */
    public void setHostnameAndPassword(String hostname, String password) {
        this.server = "http://" + hostname + ":8080";
        this.password = password;
    }

    /**
     * Sends HTTP request to get board
     *
     * @param boardId the id of the board to load
     * @return the board whose it was
     */
    public Board getBoard(long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("boards/{id}") //
            .resolveTemplate("id", boardId) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Board.class);
    }

    /**
     * Check if a board exists on the server side
     * @param boardId the board to check for
     * @return whether it exists
     */
    public boolean boardExists(long boardId) {
        var res = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("boards/{id}")
                .resolveTemplate("id", boardId) //
                .request()
                .head();

        if (res.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends HTTP request to server to add a new card
     *
     * @param card the card to add to the database
     * @return The Card that was added to the database
     */
    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("cards/new")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * send the server Put request to add a new board to the database
     *
     * @param board the board to add to the database
     * @return the board that was added
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("boards/new")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }
    /**
     * Sends HTTP request to change card's details
     *
     * @param card the card to change
     * @return The updated instance of Card
     */
    public Card editCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("cards/{id}")
            .resolveTemplate("id", card.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Sends HTTP request to move a card
     *
     * @param cardId   the id of the card to move
     * @param newListId the id of the list to move the card to
     * @param oldListId the id of the list it is moved from
     * @param newIndex the index of the card in the new list
     * @return true if the card was moved successfully, false otherwise
     */
    public boolean moveCard(long cardId, long newListId, long oldListId, long newIndex) {

        MoveCardMessage message = new MoveCardMessage(cardId, newListId, oldListId, newIndex);

        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("cards/move")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(message, APPLICATION_JSON), Boolean.class);
    }

    /**
     * Sends HTTP request to add a new CardList to the database
     *
     * @param cardList the Card
     * @return the new CardList
     */
    public CardList addCardList(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(server).path("lists/new") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .put(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    /**
     * Sends HTTP request to edit a CardList in the database
     *
     * @param cardList the CardList containing the changes
     * @return the CardList saved in the database
     */
    public CardList editCardList(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("lists/{id}")
            .resolveTemplate("id", cardList.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    /**
     * send the server Delete request to remove a card from the database
     *
     * @param card the card to remove from the database
     * @return true if card was deleted from the database, false otherwise
     */
    public boolean deleteCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server)
            .path("cards/{id}")
            .resolveTemplate("id", card.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete().readEntity(Boolean.class);
    }

    /**
     * Deletes cardList from server
     *
     * @param cardList cardList to delete
     */
    public void deleteCardList(CardList cardList) {
        ClientBuilder.newClient(new ClientConfig())
            .target(server)
            .path("lists/{id}")
            .resolveTemplate("id", cardList.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * send the server Get request for all the cards of a specific list
     *
     * @param listId id of the list to get the cards from
     * @return list of all the cards in the requested list
     */
    public List<Card> getCardsByList(long listId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server)
            .path("cards/list/{id}")
            .resolveTemplate("id", listId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    /**
     * Get all the board that exists
     * (note that this also sends all lists and cards,
     * this should probably be changed in the future)
     *
     * @return a list of all existing boards
     */
    public List<Board> getAllBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("boards/all")
            .   request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("password", password)
                .get(new GenericType<>() {});
    }
    /**
     * Get all the board with the given ids
     * (note that this also sends all lists and cards,
     * this should probably be changed in the future)
     *
     * @param ids the ids of the boards
     * @return a list of all found boards
     */
    public List<Board> getAllBoards(List<Long> ids) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("boards/find")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(ids, APPLICATION_JSON))
                .readEntity(new GenericType<>() {
                });
    }

    /**
     * Update the board details.
     * @param updatedBoard board with changed details,
     *                     shouldn't contain cardlists for performance reasons
     */
    public void updateBoard(Board updatedBoard) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("boards/update")
                .request()
                .post(Entity.entity(updatedBoard, APPLICATION_JSON));
    }

    /**
     * Delete a board
     * @param boardId the id of the board that should be deleted
     */
    public void deleteBoard(long boardId) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("boards/" + boardId)
                .request()
                .delete();
    }

    /**
     * Check whether the password gives a valid admin authentication
     * @param hostname the server for which to check if it is authorized
     * @param password the password
     * @return whether it's a valid password
     */
    public boolean isAuthenticated(String hostname, String password) throws ProcessingException {
        String server = "http://" + hostname + ":8080";
        var res = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("authenticate")
                .request()
                .header("password", password)
                .post(null);

        return res.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL);
    }

    /**
     * Check whether there is a password,
     * i.e. check whether it's in admin mode.
     * @return whether there is a password set.
     */
    public boolean hasPassword() {
        return this.password != null;
    }

    /**
     * Get the hostname of the server
     * @return the hostname
     */
    public String getHostname() {
        return this.server;
    }
}