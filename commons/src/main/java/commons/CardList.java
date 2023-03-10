package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CardList {
    long cardListId;
    String cardListTitle;
    long firstCardId;

    public CardList() {
    }

    public CardList(long cardListId, String cardListTitle, long firstCardId) {
        this.cardListId = cardListId;
        this.cardListTitle = cardListTitle;
        this.firstCardId = firstCardId;
    }

    public void renameCardList(String newName) {
        this.cardListTitle = newName;
    }

    @Id
    public long getCardListId() {
        return cardListId;
    }

    public void setCardListId(long cardListId) {
        this.cardListId = cardListId;
    }

    public String getCardListTitle() {
        return cardListTitle;
    }

    public void setCardListTitle(String cardListTitle) {
        this.cardListTitle = cardListTitle;
    }

    public long getFirstCardId() {
        return firstCardId;
    }

    public void setFirstCardId(long firstCardId) {
        this.firstCardId = firstCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList cardList = (CardList) o;
        return cardListId == cardList.cardListId && firstCardId == cardList.firstCardId && Objects.equals(cardListTitle, cardList.cardListTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardListId, cardListTitle, firstCardId);
    }
}
