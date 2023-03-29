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
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {


    private String server;

    /**
     * Set the hostname of the server and then connect to it
     * @param hostname the hostname
     */
    public void setHostnameAndConnect(String hostname) {
        System.out.println("Connecting to server: " + hostname);
        this.server = "http://" + hostname + ":8080";

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
     * Sends HTTP request to server to add a new card
     *
     * @param card the card to add to the database
     */
    public void addCard(Card card) {
        ClientBuilder.newClient(new ClientConfig())
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
     */
    public void addBoard(Board board) {
        ClientBuilder.newClient(new ClientConfig())
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
     * @param card the card to remove from the database
     */
    public void deleteCard(Card card) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("cards/{id}")
                .resolveTemplate("id", card.getId())
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
    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("boards/all")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

}