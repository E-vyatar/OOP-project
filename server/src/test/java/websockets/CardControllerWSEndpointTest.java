package websockets;

import commons.Card;
import commons.messages.MoveCardMessage;
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

/**
 * Test the Web Socket endpoints for /cards path (in CardController)
 */
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

    /**
     * Tests the /cards/new socket endpoint
     *
     * @throws Exception for the .get() in websocket connection
     */
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

    /**
     * Tests the /cards/edit socket endpoint
     *
     * @throws Exception for the .get() in websocket connection
     */
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

    /**
     * Tests the /cards/delete socket endpoint
     *
     * @throws Exception for the .get() in websocket connection
     */
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

    /**
     * Tests the /cards/move socket endpoint.
     * <p>
     *     Test Case: card does not exist
     * </p>
     *
     * @throws Exception for the .get() in websocket connection
     */
    @Test
    void moveCardMessage1() throws Exception {
        MoveCardMessage message = new MoveCardMessage(0, 1, 0, 1);

        when(cardRepository.findById(message.getCardId())).thenReturn(Optional.empty());

        BlockingQueue<MoveCardMessage> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(
                String.format("ws://localhost:%d/websocket", port),
                new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/move", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MoveCardMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((MoveCardMessage) payload);
            }
        });

        session.send("/app/cards/move", message);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertNotNull(blockingQueue.peek());
                Assertions.assertFalse(blockingQueue.poll().isMoved());
            });

        verify(cardRepository, times(1)).findById(message.getCardId());
        verifyNoMoreInteractions(cardRepository);
    }

    /**
     * Tests the /cards/move socket endpoint.
     * <p>
     *     Test Case: card is moved to different list
     * </p>
     *
     * @throws Exception for the .get() in websocket connection
     */
    @Test
    void moveCardMessage2() throws Exception {
        MoveCardMessage message = new MoveCardMessage(0, 1, 0, 1);
        Card card = new Card(0, 0, 0, "Card", 0);

        when(cardRepository.findById(message.getCardId())).thenReturn(Optional.of(card));
        doNothing()
            .when(cardRepository)
            .moveAllCardsHigherEqualThanIndexUp(message.getNewListId(), message.getNewIndex());
        doNothing()
            .when(cardRepository)
            .moveAllCardsHigherThanIndexDown(card.getListId(), card.getIdx());
        when(cardRepository.save(card)).thenReturn(null);

        BlockingQueue<MoveCardMessage> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(
                String.format("ws://localhost:%d/websocket", port),
                new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/move", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MoveCardMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((MoveCardMessage) payload);
            }
        });

        session.send("/app/cards/move", message);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertEquals(1, card.getIdx());
                Assertions.assertEquals(1, card.getListId());
                Assertions.assertNotNull(blockingQueue.peek());
                Assertions.assertTrue(blockingQueue.poll().isMoved());
            });

        verify(cardRepository, times(1)).findById(message.getCardId());
        verify(cardRepository, times(1))
            .moveAllCardsHigherEqualThanIndexUp(1, 1);
        verify(cardRepository, times(1))
            .moveAllCardsHigherThanIndexDown(0, 0);
        verify(cardRepository, times(1)).save(card);
        verifyNoMoreInteractions(cardRepository);
    }

    /**
     * Tests the /cards/move socket endpoint.
     * <p>
     *     Test Case: card moved to same index & same list
     * </p>
     *
     * @throws Exception for the .get() in websocket connection
     */
    @Test
    void moveCardMessage3() throws Exception {
        MoveCardMessage message = new MoveCardMessage(0, 0, 0, 0);
        Card card = new Card(0, 0, 0, "Card", 0);

        when(cardRepository.findById(message.getCardId())).thenReturn(Optional.of(card));

        BlockingQueue<MoveCardMessage> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(
                String.format("ws://localhost:%d/websocket", port),
                new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/move", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MoveCardMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((MoveCardMessage) payload);
            }
        });

        session.send("/app/cards/move", message);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertEquals(0, card.getIdx());
                Assertions.assertEquals(0, card.getIdx());
                Assertions.assertNotNull(blockingQueue.peek());
                Assertions.assertTrue(blockingQueue.poll().isMoved());
            });

        verify(cardRepository, times(1)).findById(message.getCardId());
        verifyNoMoreInteractions(cardRepository);
    }

    /**
     * Tests the /cards/move socket endpoint.
     * <p>
     *     Test Case: card moved to same list but index lower
     * </p>
     *
     * @throws Exception for the .get() in websocket connection
     */
    @Test
    void moveCardMessage4() throws Exception {
        MoveCardMessage message = new MoveCardMessage(0, 0, 0, 0);
        Card card = new Card(0, 0, 0, "Card", 1);

        when(cardRepository.findById(message.getCardId())).thenReturn(Optional.of(card));
        doNothing().when(cardRepository).updateIdxBetweenUp(0, 0, 1);
        when(cardRepository.save(card)).thenReturn(null);

        BlockingQueue<MoveCardMessage> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(
                String.format("ws://localhost:%d/websocket", port),
                new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/move", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MoveCardMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((MoveCardMessage) payload);
            }
        });

        session.send("/app/cards/move", message);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertEquals(0, card.getIdx());
                Assertions.assertEquals(0, card.getListId());
                Assertions.assertNotNull(blockingQueue.peek());
                Assertions.assertTrue(blockingQueue.poll().isMoved());
            });

        verify(cardRepository, times(1)).findById(message.getCardId());
        verify(cardRepository, times(1))
            .updateIdxBetweenUp(0, 0, 1);
        verify(cardRepository, times(1)).save(card);
        verifyNoMoreInteractions(cardRepository);
    }

    /**
     * Tests the /cards/move socket endpoint.
     * <p>
     *     Test Case: card moved to same list but index higher
     * </p>
     *
     * @throws Exception for the .get() in websocket connection
     */
    @Test
    void moveCardMessage5() throws Exception {
        MoveCardMessage message = new MoveCardMessage(0, 0, 0, 1);
        Card card = new Card(0, 0, 0, "Card", 0);

        when(cardRepository.findById(message.getCardId())).thenReturn(Optional.of(card));
        doNothing().when(cardRepository).updateIdxBetweenDown(0, 0, 1);
        when(cardRepository.save(card)).thenReturn(null);

        BlockingQueue<MoveCardMessage> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
            .connect(
                String.format("ws://localhost:%d/websocket", port),
                new StompSessionHandlerAdapter() {})
            .get(1, SECONDS);

        session.subscribe("/topic/cards/move", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return MoveCardMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((MoveCardMessage) payload);
            }
        });

        session.send("/app/cards/move", message);

        await()
            .atMost(1, SECONDS)
            .untilAsserted(() -> {
                Assertions.assertEquals(1, card.getIdx());
                Assertions.assertEquals(0, card.getListId());
                Assertions.assertNotNull(blockingQueue.peek());
                Assertions.assertTrue(blockingQueue.poll().isMoved());
            });

        verify(cardRepository, times(1)).findById(message.getCardId());
        verify(cardRepository, times(1))
            .updateIdxBetweenDown(0, 0, 1);
        verify(cardRepository, times(1)).save(card);
        verifyNoMoreInteractions(cardRepository);
    }
}
