package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CardList {
    @Id
    long id;
    String title;
    long idx;

    public CardList() {
    }

    public CardList(long id, String title) {
        this.id = id;
        this.title = title;
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

}
