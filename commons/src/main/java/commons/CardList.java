package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CardList {
    long id;
    String title;
    long firstCardId;

    public CardList() {
    }

    public CardList(long id, String title, long firstCardId) {
        this.id = id;
        this.title = title;
        this.firstCardId = firstCardId;
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
        return id == cardList.id && firstCardId == cardList.firstCardId && Objects.equals(title, cardList.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, firstCardId);
    }
}
