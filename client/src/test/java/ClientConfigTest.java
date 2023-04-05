import client.ClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
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
        assertEquals(List.of(), clientConfig.getIds(LOCALHOST),
                "Config should initialize with the board of id 0");
    }

    @Test
    public void testAddItem() {
        clientConfig.addBoard(LOCALHOST, 13L);
        assertEquals(List.of(13L), clientConfig.getIds(LOCALHOST), "board with id 13 should have been added.");

        clientConfig.addBoard(LOCALHOST, 91L);
        assertEquals(List.of(13L, 91L), clientConfig.getIds(LOCALHOST), "board with id 91 should have been added.");
    }
    @Test
    public void testRemove(){
        clientConfig.addBoard(LOCALHOST, 13L);
        clientConfig.addBoard(LOCALHOST, 21L);
        clientConfig.removeBoard(LOCALHOST, 13L);
        assertEquals(List.of(21L), clientConfig.getIds(LOCALHOST), "board with id 13 should have been removed.");

        clientConfig.removeBoard(LOCALHOST, 13L);
        assertEquals(List.of(21L), clientConfig.getIds(LOCALHOST), "removing board with id 13 should not have any effect.");

        clientConfig.removeBoard(LOCALHOST, 21L);
        assertEquals(List.of(), clientConfig.getIds(LOCALHOST), "board with id 21 should have been removed.");
    }
    @Test
    public void testTwoServers(){
        String otherServer = "http://example.com:9191";
        clientConfig.addBoard(otherServer, 9L);
        clientConfig.addBoard(LOCALHOST, 14L);
        assertEquals(List.of(9L), clientConfig.getIds(otherServer), "only board with id 9 should have been added");
        assertEquals(List.of(14L), clientConfig.getIds(LOCALHOST), "board with id 14 should have been added.");

        clientConfig.removeBoard(otherServer, 14L);
        assertEquals(List.of(14L), clientConfig.getIds(LOCALHOST), "Board with id 14 should not have been removed from localhost");
    }

    @Test
    public void testEqualsTrue() {
        ClientConfig clientConfig1 = new ClientConfig();
        ClientConfig clientConfig2 = new ClientConfig();

        assertEquals(clientConfig1, clientConfig2,
                "Empty clientconfigs should be equal");

        clientConfig1.addBoard(LOCALHOST, 4L);
        clientConfig2.addBoard(LOCALHOST, 4L);
        assertEquals(clientConfig1, clientConfig2,
                "Clientconfigs with the same board should be equals");

        clientConfig1.addBoard(LOCALHOST, 52L);
        clientConfig2.addBoard(LOCALHOST, 52L);
        assertEquals(clientConfig1, clientConfig2,
                "Clientconfigs with the same board should be equals");
    }

    @Test
    public void testEqualsFalse() {
        String otherServer = "http://example.com:9191";
        ClientConfig clientConfig1 = new ClientConfig();
        ClientConfig clientConfig2 = new ClientConfig();

        clientConfig1.addBoard(LOCALHOST, 4L);
        assertNotEquals(clientConfig1, clientConfig2,
                "A difference in boards should result in inequality");

        clientConfig2.addBoard(otherServer, 4L);

        assertNotEquals(clientConfig1, clientConfig2,
                "Clientconfigs with the same board but in different servers should be unequal");

        assertNotEquals(clientConfig1, null,
                "clientconfig should always be unequal to null");

        assertNotEquals(clientConfig1, new Object(),
                "clientconfig should be unequal to an Object that is not clientConfig");
    }

    @Test
    public void testHashCode() {
        ClientConfig clientConfig1 = new ClientConfig();
        ClientConfig clientConfig2 = new ClientConfig();

        assertEquals(clientConfig1.hashCode(), clientConfig2.hashCode(),
                "Equal clientconfigs should have the same hashcode");

        clientConfig1.addBoard(LOCALHOST, 4L);
        clientConfig2.addBoard(LOCALHOST, 4L);
        assertEquals(clientConfig1, clientConfig2,
                "Equal clientconfigs should have the same hashcode");

        clientConfig1.addBoard(LOCALHOST, 52L);
        clientConfig2.addBoard(LOCALHOST, 52L);
        assertEquals(clientConfig1, clientConfig2,
                "Equal clientconfigs should have the same hashcode");
    }
    @Test
    public void testReadWrite(@TempDir File tempDir) throws IOException, ClassNotFoundException {
        clientConfig.addBoard(LOCALHOST, 4L);
        clientConfig.addBoard(LOCALHOST, 49L);

        File file = new File(tempDir, "testfile");

        clientConfig.saveConfig(file);

        ClientConfig read = new ClientConfig();
        read.readConfig(file);

        assertEquals(clientConfig, read,
                "reading and writing should result in the same object");
    }
}
