package websockets;

import commons.Card;
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
import server.api.CardController;
import server.database.CardRepositroy;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {CardController.class, WebSocketConfig.class, Main.class})
public class CardControllerWSEndpointTest {

    @LocalServerPort
    private Integer port;

    @MockBean
    private CardRepositroy cardRepository;

    private WebSocketStompClient webSocketStompClient;


    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Test
    void addMessage() throws Exception {
        Card card = new Card(1L, 0L, 1L, "Card 1", 0);

        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardRepository.countByListId(any(Long.class))).thenReturn(0L);

        BlockingQueue<Card> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/new", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Card.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Card) payload);
            }
        });

        session.send("/app/cards/new", card);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> Assertions.assertEquals(card, blockingQueue.poll()));

        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardRepository, times(1)).countByListId(any(Long.class));
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void updateCardMessage() throws Exception {
        Card card1 = new Card(1L, 1L, 1L, "Card 1", 0);
        Card card2 = new Card(1L, 1L, 1L, "New Title", 0);

        when(cardRepository.findById(card2.getId())).thenReturn(Optional.of(card1));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card cardTemp = invocation.getArgument(0);
            if(card1.getId() == cardTemp.getId()) {
                card1.setTitle(cardTemp.getTitle());
            }
            return card1;
        });

        BlockingQueue<Card> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/edit", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Card.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Card) payload);
            }
        });

        session.send("/app/cards/edit", card2);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> Assertions.assertEquals(card2, blockingQueue.poll()));

        verify(cardRepository, times(1)).findById(card2.getId());
        verify(cardRepository, times(1)).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void deleteMessage() throws Exception {

        final boolean[] exists = {true};

        doAnswer(invocation -> {
            exists[0] = false;
            return null;
        }).when(cardRepository).deleteById(0L);

        BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/delete", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Long.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Long) payload);
            }
        });

        session.send("/app/cards/delete", 0L);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertEquals(0L, blockingQueue.poll());
                Assertions.assertFalse(exists[0]);
            });

        verify(cardRepository, times(1)).deleteById(0L);
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void moveCardMessage() throws Exception {
        // TODO
    }
}
