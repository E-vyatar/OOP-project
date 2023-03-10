package commons;

import javax.persistence.Id;

public class Card {

    @Id
    String id;
    String listId;
    String title;
    String nextCardId;
    String boardId;

    public Card() {
    }

    public Card(String id, String listId, String title, String nextCardId, String boardId) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.nextCardId = nextCardId;
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", cardListId='" + listId + '\'' +
                ", title='" + title + '\'' +
                ", nextCardId='" + nextCardId + '\'' +
                ", boardId='" + boardId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String cardId) {
        this.id = cardId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String cardListId) {
        this.listId = cardListId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String cardTitle) {
        this.title = cardTitle;
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
}
