package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CardList {
    @Id
    long id;
    String title;
    long idx;
    long boardId;

    List<Card> cards;

    public CardList() {
    }

    public CardList(long id, String title, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
        this.cards = new ArrayList<>();
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public void renameCardList(String newName) {
        this.title = newName;
    }

    @Id
    public long getId() {
        return id;
    }

    public void setId(long cardListId) {
        this.id = cardListId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String cardListTitle) {
        this.title = cardListTitle;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    /**
     * Get the cards inside this CardList
     * @return a list of cards
     */
    @OneToMany(mappedBy = "listId")
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Set the cards inside this CardList
     * @param cards the list of cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public static CardList createNewCardList(String cardListTitle, long firstCardId) {
        // Some Server side code to create a new cardList and get the ID.
        // TODO: Implement this.

        long cardListId = -1;

        return new CardList(cardListId, cardListTitle, firstCardId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardList)) return false;
        CardList cardList = (CardList) o;
        return id == cardList.id && idx == cardList.idx && boardId == cardList.boardId && Objects.equals(title, cardList.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, idx, boardId);
    }


}
