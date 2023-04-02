package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientConfig {

    private final HashMap<String, List<Long>> boardsIds;

    /**
     * Constructor for ClientConfig.
     * This remembers the saved boards.
     */
    public ClientConfig() {
        this.boardsIds = new HashMap<>();
    }

    /**
     * Add a new board to the saved config.
     * @param hostname the hostname of the server
     * @param boardId the id of the board
     */
    public void addBoard(String hostname, long boardId) {
        List<Long> ids = getIds(hostname);
        ids.add(boardId);
    }

    /**
     * Remove a board from the config
     * @param hostname the hostname of the server
     * @param boardId the id of the board
     */
    public void removeBoard(String hostname, Long boardId) {
        List<Long> ids = getIds(hostname);
        ids.remove(boardId);
    }

    /**
     * Get all boards for a specific server.
     * If there are no saved boards,
     * we return a list with the board id zero by default.
     *
     * @param hostname the hostname of the server
     * @return the list of all ids
     */
    public List<Long> getIds(String hostname) {
        List<Long> boards = boardsIds.get(hostname);
        if (boards == null) {
            boards = new ArrayList<>();
            boards.add(0L);
            boardsIds.put(hostname, boards);
        }
        return boards;
    }
}
