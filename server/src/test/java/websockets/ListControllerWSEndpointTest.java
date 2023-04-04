package websockets;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import server.Main;
import server.WebSocketConfig;
import server.api.ListController;
import server.database.ListRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ListController.class, WebSocketConfig.class, Main.class})
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

    // TODO: write test methods for editListMessage, addListMessage, deleteListMessage
}
