package client.scenes.service;

import client.utils.SocketsUtils;
import com.google.inject.Inject;
import commons.CardList;

/**
 * Service for {@link client.scenes.RenameListPopupCtrl}.
 * Used to separate testable methods from JavaFX.
 */
public class RenameListService {

    private final SocketsUtils socket;
    private CardList cardList;

    /**
     * Constructor.
     *
     * @param socket the SocketUtils
     */
    @Inject
    public RenameListService(SocketsUtils socket) {
        this.socket = socket;
    }

    /**
     * Sends a request to the websocket to change the title
     * of the CardList to the given {@code String}.
     *
     * @param title the new title
     */
    public void save(String title) {
        cardList.setTitle(title);
        socket.send("/app/lists/edit", cardList);
    }

    /**
     * Setter for cardList
     *
     * @param cardList the CardList for the popup controller
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }


    /**
     * Getter for the title of the cardList
     *
     * @return the title of the cardList
     */
    public String getTitle() {
        return cardList.getTitle();
    }

    /**
     * Getter for list ID
     *
     * @return the ID of the cardList
     */
    public long getListId() {
        return cardList.getId();
    }
}
