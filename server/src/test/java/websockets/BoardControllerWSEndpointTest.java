package websockets;

import commons.Board;
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
import server.api.BoardController;
import server.database.BoardRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = {BoardController.class, WebSocketConfig.class, Main.class})
public class BoardControllerWSEndpointTest {
    @LocalServerPort
    private Integer port;
    @MockBean
    private BoardRepository boardRepository;
    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setup(){
        this.webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Test
    void addBoardMessage() throws ExecutionException, InterruptedException, TimeoutException {
        Board board = new Board();
        board.setId(1L);
        board.setTitle("Board 1");

        CardList todo = new CardList(0L, "TODO", 0, 1L);
        CardList doing = new CardList(1L, "Doing", 1, 1L);
        CardList done = new CardList(2L, "Done", 2, 1L);

        List<CardList> cardLists = List.of(todo, doing, done);
        board.getCardLists().addAll(cardLists);

        when(boardRepository.save(any(Board.class))).thenReturn(board);

        BlockingQueue<Board> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, TimeUnit.SECONDS);
        session.subscribe("/topic/boards/new", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Board.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Board) payload);
            }
        });

        session.send("/app/boards/new", board);
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> Assertions.assertEquals(board, blockingQueue.poll()));
        verify(boardRepository, times(1)).save(any(Board.class));
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    void deleteBoardMessage() throws ExecutionException, InterruptedException, TimeoutException {
        final boolean[] exists = {true};
        doAnswer(invocation -> {
            exists[0] = false;
            return null;
        }).when(boardRepository).deleteById(0L);

        BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, TimeUnit.SECONDS);
        session.subscribe("/topic/boards/delete", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Long.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Long) payload);
            }
        });

        session.send("/app/boards/delete", 0L);

        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() ->{
                    Assertions.assertEquals(0L, blockingQueue.poll());
                    Assertions.assertFalse(exists[0]);
                });

        verify(boardRepository, times(1)).deleteById(0L);
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void editMessage() throws ExecutionException, InterruptedException, TimeoutException {
        Board board1 = new Board();
        board1.setId(1L);
        board1.setTitle("Board 1");

        CardList todo = new CardList(0L, "TODO", 0, 1L);
        CardList doing = new CardList(1L, "Doing", 1, 1L);
        CardList done = new CardList(2L, "Done", 2, 1L);

        List<CardList> cardLists = List.of(todo, doing, done);
        board1.getCardLists().addAll(cardLists);

        Board board2 = new Board();
        board2.setId(1L);
        board2.setTitle("New Board");
        board2.getCardLists().addAll(cardLists);

        when(boardRepository.findById(board2.getId())).thenReturn(Optional.of(board1));
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> {
            Board boardtemp = invocation.getArgument(0);
            if(board1.getId() == boardtemp.getId()){
                board1.setTitle(boardtemp.getTitle());
            }
            return board1;
        });

        BlockingQueue<Board> blockingQueue = new ArrayBlockingQueue<>(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(1, TimeUnit.SECONDS);
        session.subscribe("/topic/boards/edit", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Board.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((Board) payload);
            }
        });

        session.send("/app/boards/edit", board2);

        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> Assertions.assertEquals(board2, blockingQueue.poll()));

        verify(boardRepository, times(1)).findById(board2.getId());
        verify(boardRepository, times(1)).save(any(Board.class));
        verifyNoMoreInteractions(boardRepository);
    }
}
