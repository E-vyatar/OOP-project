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
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    private final StompSession session = connect("ws://localhost:8080/websocket");

    /**
     * This method gets the board from the server,
     * @param boardId the id of the board to load
     * @return the board whose it was
     */
    public Board getBoard(long boardId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("boards/{id}") //
                .resolveTemplate("id", boardId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Board.class);
    }

    /**
     * send the server Put request to add a new card to the database
     * @param card the card to add to the database
     */
    public Card createNewCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("cards/new") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * send the server Post request to change card's details
     * @param card the card to change
     */
    public Card editCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("cards/{id}")
                .resolveTemplate("id", card.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * @param cardList
     *
     * This method is used to add a new list to the database
     */
    public CardList createNewCardList(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("lists/new") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }
    
    public CardList editCardList(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("lists/{id}")
                .resolveTemplate("id", cardList.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }





    /**
     * send the server Get request for all the cards of a specific list
     * @param listId id of the list to get the cards from
     * @return list of all the cards in the requested list
     */
    public List<Card> getCardsByList(long listId) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("cards/list/{id}") //
                .resolveTemplate("id", listId)
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }

    /**
     * @param url address
     *
     * @return
     */
    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param destination destination for the upcoming messages
     * @param type  class type
     * @param consumer  the subscriber
     * @param <T> generic class
     */
    public <T> void registerMessages(String destination, Class <T> type, Consumer<T> consumer){
        session.subscribe(SERVER, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }
}