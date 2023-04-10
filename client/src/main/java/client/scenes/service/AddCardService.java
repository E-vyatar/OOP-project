package client.scenes.service;

import client.utils.SocketsUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;

/**
 * Service for AddCardCtrl containing all testable parts of the code.
 */
public class AddCardService {

    private final SocketsUtils socket;

    private CardList cardList;

    /**
     * Constructor.
     *
     * @param socket the SocketUtils
     */
    @Inject
    public AddCardService(SocketsUtils socket) {
        this.socket = socket;
    }

    /**
     * Getter for cardList
     *
     * @return returns the cardList stored in this service
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * Setter for cardList
     *
     * @param cardList the new cardList to be set
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    /**
     * Creates a card using the given title and sends request to server
     *
     * @param title the title of the new card
     * @throws WebApplicationException from the send method in SocketUtils (used for the alert)
     */
    public void createAndSend(String title) throws WebApplicationException {
        Card card = new Card(cardList.getId(), cardList.getBoardId(), title, -1);
        socket.send("/app/cards/new", card);
    }
}
