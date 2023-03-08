package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Card {

    @Id
    long cardId;
    long cardListId;
    String cardTitle;
    long nextCardId;
    long boardId;

    public Card() {
    }
    public Card(long cardId, long cardListId, String cardTitle, long nextCardId, long boardId) {
        this.cardId = cardId;
        this.cardListId = cardListId;
        this.cardTitle = cardTitle;
        this.nextCardId = nextCardId;
        this.boardId = boardId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getCardListId() {
        return cardListId;
    }

    public void setCardListId(long cardListId) {
        this.cardListId = cardListId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public long getNextCardId() {
        return nextCardId;
    }

    public void setNextCardId(long nextCardId) {
        this.nextCardId = nextCardId;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardId == card.cardId && cardListId == card.cardListId && nextCardId == card.nextCardId && boardId == card.boardId && Objects.equals(cardTitle, card.cardTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardListId, cardTitle, nextCardId, boardId);
    }
}
