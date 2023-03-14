package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card {

    @Id
    long id;
    long listId;
    String title;
    long nextCardId;
    long boardId;

    public Card() {
    }

    public Card(long id, long listId, String title, long nextCardId, long boardId) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.nextCardId = nextCardId;
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Card{" + "id='" + id + '\'' + ", cardListId='" + listId + '\'' + ", title='" + title + '\'' + ", nextCardId='" + nextCardId + '\'' + ", boardId='" + boardId + '\'' + '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long cardId) {
        this.id = cardId;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long cardListId) {
        this.listId = cardListId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String cardTitle) {
        this.title = cardTitle;
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
}
