package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CardList {
    @Id
    long id;
    String title;
    long idx;
    long boardId;

    public CardList() {
    }

    public CardList(long id, String title, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
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
