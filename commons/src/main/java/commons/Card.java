package commons;

import java.util.Objects;

public class Card {

    String cardId;
    String cardListId;
    String cardTitle;
    String nextCardId;
    String boardId;

    public Card(String cardId, String cardListId, String cardTitle, String nextCardId, String boardId) {
        this.cardId = cardId;
        this.cardListId = cardListId;
        this.cardTitle = cardTitle;
        this.nextCardId = nextCardId;
        this.boardId = boardId;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardListId() {
        return cardListId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getNextCardId() {
        return nextCardId;
    }

    public String getBoardId() {
        return boardId;
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
