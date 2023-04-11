package client.scenes.service;

import client.utils.SocketsUtils;
import com.google.inject.Inject;

/**
 * Service for {@link client.scenes.DeleteListPopupCtrl}.
 * Used to separate testable methods from JavaFX.
 */
public class DeleteListService {
    private static final int maxTitleLength = 60;
    private long listId;
    private final SocketsUtils socket;

    /**
     * Constructor.
     *
     * @param socket the SocketUtils
     */
    @Inject
    public DeleteListService(SocketsUtils socket) {
        this.socket = socket;
    }

    /**
     * Static method that creates the desired text for the popup label.
     *
     * @param listTitle the title of the list to be shown on label
     * @return a sting that will be shown on label
     */
    public static String generateLabelText(String listTitle) {
        if (listTitle.length() > maxTitleLength) {
            listTitle = listTitle.substring(0, maxTitleLength) + "...";
        }
        return "Are you sure you want to delete '" + listTitle + "' list? " +
            "It will delete all cards within it.";
    }

    /**
     * sends a request to the websocket to delete list.
     */
    public void delete() {
        socket.send("/app/lists/delete", listId);
    }

    /**
     * Getter for listId
     *
     * @return the list ID
     */
    public long getListId() {
        return listId;
    }

    /**
     * Setter for list ID
     *
     * @param listId teh new list ID
     */
    public void setListId(long listId) {
        this.listId = listId;
    }
}
