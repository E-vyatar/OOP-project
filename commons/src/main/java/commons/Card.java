package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Card {

    String cardId;
    String cardListId;
    String cardTitle;
    String nextCardId;
    String boardId;

    public Card() {
    }
    public Card(String cardId, String cardListId, String cardTitle, String nextCardId, String boardId) {
        this.cardId = cardId;
        this.cardListId = cardListId;
        this.cardTitle = cardTitle;
        this.nextCardId = nextCardId;
        this.boardId = boardId;
    }

    @Id
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardListId() {
        return cardListId;
    }

    public void setCardListId(String cardListId) {
        this.cardListId = cardListId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getNextCardId() {
        return nextCardId;
    }

    public void setNextCardId(String nextCardId) {
        this.nextCardId = nextCardId;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardId, card.cardId) && Objects.equals(cardListId, card.cardListId) && Objects.equals(cardTitle, card.cardTitle) && Objects.equals(nextCardId, card.nextCardId) && Objects.equals(boardId, card.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardListId, cardTitle, nextCardId, boardId);
    }


}
