package commons;

import java.util.Objects;

public class CardList {
    String cardListId;
    String cardListTitle;
    String firstCardId;

    public CardList(String cardListId, String cardListTitle, String firstCardId) {
        this.cardListId = cardListId;
        this.cardListTitle = cardListTitle;
        this.firstCardId = firstCardId;
    }

    public String getCardListId() {
        return cardListId;
    }

    public String getCardListTitle() {
        return cardListTitle;
    }

    public String getFirstCardId() {
        return firstCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList cardList = (CardList) o;
        return Objects.equals(cardListId, cardList.cardListId) && Objects.equals(cardListTitle, cardList.cardListTitle) && Objects.equals(firstCardId, cardList.firstCardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardListId, cardListTitle, firstCardId);
    }
}
