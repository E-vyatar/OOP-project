package websockets;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import server.Main;
import server.WebSocketConfig;
import server.api.ListController;
import server.database.ListRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ListController.class, WebSocketConfig.class, Main.class, Card.class})
public class ListControllerWSEndpointTest {

    @LocalServerPort
    private Integer port;

    @MockBean
    private ListRepository listRepository;

    private WebSocketStompClient webSocketStompClient;


    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
    }


    @Test
    void addListMessage() throws Exception {

        List<Card> cardList = new ArrayList<>();

        CardList list = new CardList();
        list.setId(1L);
        list.setTitle("List 1");
        list.setIdx(0);
        list.setBoardId(1L);
        list.setCards(cardList);

        when(listRepository.save(any(CardList.class))).thenReturn(list);
        when(listRepository.countByBoardId(any(Long.class))).thenReturn(1L);

        BlockingQueue<CardList> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, SECONDS);
        session.subscribe("/topic/lists/new", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CardList.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((CardList) payload);
            }
        });

        session.send("/app/lists/new", list);
        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> Assertions.assertEquals(list, blockingQueue.poll()));

        verify(listRepository, times(1)).save(any(CardList.class));
        verify(listRepository, times(1)).countByBoardId(any(Long.class));
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    void editMessage() throws ExecutionException, InterruptedException, TimeoutException {
        List<Card> def = new ArrayList<>();

        CardList cardList1 = new CardList();
        cardList1.setId(1L);
        cardList1.setTitle("List 1");
        cardList1.setIdx(0);
        cardList1.setBoardId(1L);
        cardList1.setCards(def);

        CardList cardList2 = new CardList();
        cardList2.setCards(def);
        cardList2.setId(1L);
        cardList2.setTitle("New List Title");
        cardList2.setIdx(0);
        cardList2.setBoardId(1L);

        CardList cardList3 = new CardList();
        cardList3.setCards(def);
        cardList3.setId(100L);
        cardList3.setTitle("List Title");
        cardList3.setIdx(45343);
        cardList3.setBoardId(1L);

        when(listRepository.existsById(cardList3.getId())).thenReturn(false);

        when(listRepository.existsById(cardList2.getId())).thenReturn(true);
        when(listRepository.findById(cardList2.getId())).thenReturn(Optional.of(cardList1));
        when(listRepository.save(any(CardList.class))).thenAnswer(invocation -> {
            CardList cardListTemp = invocation.getArgument(0);
            if(cardList1.getId() == cardList2.getId()){
                cardList1.setTitle(cardListTemp.getTitle());
            }
            return cardList1;
        });

        BlockingQueue<CardList> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, SECONDS);
        session.subscribe("/topic/lists/edit/" + cardList2.getBoardId(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CardList.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((CardList) payload);
            }
        });

        // test not null
        session.send("/app/lists/edit", cardList2);

        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> Assertions.assertEquals(cardList2, blockingQueue.poll()));


        //test null
        session.send("/app/lists/edit", cardList3);

        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> Assertions.assertEquals(null, blockingQueue.poll()));

        verify(listRepository, times(1)).existsById(cardList2.getId());
        verify(listRepository, times(1)).existsById(cardList3.getId());
        verify(listRepository, times(1)).save(any(CardList.class));
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    void deleteMessage() throws ExecutionException, InterruptedException, TimeoutException {
        final boolean[] exists = {true};

        when(listRepository.findById(0L)).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            exists[0] = false;
            return null;
        }).when(listRepository).deleteById(0L);

        doNothing()
                .when(listRepository).moveAllCardListsHigherThanIndexDown(0L, 0);

        BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, SECONDS);
        session.subscribe("/topic/lists/delete/" + 0L, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Long.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Long) payload);
            }
        });

        session.send("/app/lists/delete/", 0L);

        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> {
                    Assertions.assertEquals(null, blockingQueue.poll());
                });

        verifyNoMoreInteractions(listRepository);
    }
}
