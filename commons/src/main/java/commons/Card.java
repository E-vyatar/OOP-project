package commons;

import javax.persistence.Id;

public class Card {

    @Id
    String id;
    String listId;
    String title;
    long order;
    String boardId;

    public Card() {
    }

    public Card(String id, String listId, String title, long order, String boardId) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.order = order;
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", cardListId='" + listId + '\'' +
                ", title='" + title + '\'' +
                ", nextCardId='" + order + '\'' +
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

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }
}
