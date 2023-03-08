package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CardList {
    String cardListId;
    String cardListTitle;
    String firstCardId;

    public CardList() {
    }

    public CardList(String cardListId, String cardListTitle, String firstCardId) {
        this.cardListId = cardListId;
        this.cardListTitle = cardListTitle;
        this.firstCardId = firstCardId;
    }

    @Id
    public String getCardListId() {
        return cardListId;
    }
    
    public void setCardListId(String cardListId) {
        this.cardListId = cardListId;
    }

    public String getCardListTitle() {
        return cardListTitle;
    }
    public void setCardListTitle(String cardListTitle) {
        this.cardListTitle = cardListTitle;
    }

    public String getFirstCardId() {
        return firstCardId;
    }

    public void setFirstCardId(String firstCardId) {
        this.firstCardId = firstCardId;
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
