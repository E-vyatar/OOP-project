package client.scenes.service;

import client.utils.SocketsUtils;
import com.google.inject.Inject;
import commons.CardList;

public class RenameListService {

    private final SocketsUtils socket;
    private CardList cardList;

    @Inject
    public RenameListService(SocketsUtils socket, CardList cardList) {
        this.socket = socket;
        this.cardList = cardList;
    }

    public void save(String title) {
        cardList.setTitle(title);
        socket.send("/app/lists/edit", cardList);
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    public String getTitle() {
        return cardList.getTitle();
    }

    public long getListId() {
        return cardList.getId();
    }
}
