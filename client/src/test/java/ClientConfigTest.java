import client.ClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientConfigTest {

    private ClientConfig clientConfig;
    private static final String LOCALHOST = "http:://localhost:8080";

    @BeforeEach
    public void setup(){
        clientConfig = new ClientConfig();
    }

    @Test
    public void testDefaultList(){
        assertEquals(List.of(0L), clientConfig.getIds(LOCALHOST),
                "Config should initialize with the board of id 0");
    }

    @Test
    public void testAddItem() {
        clientConfig.addBoard(LOCALHOST, 13L);
        assertEquals(List.of(0L, 13L), clientConfig.getIds(LOCALHOST), "board with id 13 should have been added.");
    }
    @Test
    public void testRemove(){
        clientConfig.addBoard(LOCALHOST, 13L);
        clientConfig.addBoard(LOCALHOST, 21L);
        clientConfig.removeBoard(LOCALHOST, 13L);
        assertEquals(List.of(0L, 21L), clientConfig.getIds(LOCALHOST), "board with id 13 should have been removed.");

        clientConfig.removeBoard(LOCALHOST, 0L);
        assertEquals(List.of(21L), clientConfig.getIds(LOCALHOST), "board with id 0 should have been removed.");
    }
    @Test
    public void testTwoServers(){
        String otherServer = "http://example.com:9191";
        clientConfig.addBoard(otherServer, 9L);
        clientConfig.addBoard(LOCALHOST, 14L);
        assertEquals(List.of(0L, 9L), clientConfig.getIds(otherServer), "only board with id 9 should have been added");
        assertEquals(List.of(0L, 14L), clientConfig.getIds(LOCALHOST), "board with id 14 should have been added.");

        clientConfig.removeBoard(otherServer, 14L);
        assertEquals(List.of(0L, 14L), clientConfig.getIds(LOCALHOST), "Board with id 14 should not have been removed from localhost");
    }

}
