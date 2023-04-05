package client;

import java.io.*;
import java.util.*;

public class ClientConfig {

    private HashMap<String, List<Long>> boardsIds;

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
        List<Long> boards = boardsIds.computeIfAbsent(hostname, k -> new ArrayList<>());
        return boards;
    }

    /**
     * This method saves the config
     * @param file the file to write the config to
     * @throws IOException thrown when it can't write to the file
     */
    public void saveConfig(File file) throws IOException {
        file.createNewFile(); // only creates new file if it doesn't already exists
        FileOutputStream fos = new FileOutputStream(file, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.boardsIds);
        fos.close();
    }

    /**
     * Reads the config from the given input stream
     * @param file from where to read the config
     * @throws IOException thrown by the input strema
     * @throws ClassNotFoundException if the config contains the wrong class
     */
    public void readConfig(File file) throws IOException, ClassNotFoundException {
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.boardsIds = (HashMap<String, List<Long>>) ois.readObject();
            fis.close();
        }
    }

    /**
     * Get the default file used for storing the talio config
     * @return the file - may not exist
     */
    public File getFile() {
        String homeDir = System.getProperty("user.home");
        return new File(homeDir, ".talio_config");
    }

    /**
     * Check for object equality
     * @param o the object to compare with
     * @return whether they're equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientConfig that = (ClientConfig) o;
        return Objects.equals(boardsIds, that.boardsIds);
    }

    /**
     * Generate a hashcode for the object
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(boardsIds);
    }
}
