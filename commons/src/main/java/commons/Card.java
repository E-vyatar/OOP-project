package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    long listId;
    String title;
    long idx;
    long boardId;

    public Card() {
    }

    public Card(long id, long listId, String title, long idx, long boardId) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.idx = idx;
        this.boardId = boardId;
    }

    /**
     * Constructor id parameter (sets id = -1)
     *
     * @param listId the list's id
     * @param title the title
     * @param idx the position index
     * @param boardId the board's id
     */
    public Card(long listId, String title, long idx, long boardId) {
        this.id = -1;
        this.listId = listId;
        this.title = title;
        this.idx = idx;
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return "Card{" + "id='" + id + '\'' + ", cardListId='" + listId + '\'' + ", title='" + title + '\'' + ", nextCardId='" + idx + '\'' + ", boardId='" + boardId + '\'' + '}';
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
    
    public long getIdx() {
        return idx;
    }

    public void setIdx(long nextCardId) {
        this.idx = nextCardId;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (id != card.id) return false;
        if (listId != card.listId) return false;
        if (idx != card.idx) return false;
        if (boardId != card.boardId) return false;
        return title.equals(card.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (listId ^ (listId >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + (int) (idx ^ (idx >>> 32));
        result = 31 * result + (int) (boardId ^ (boardId >>> 32));
        return result;
    }
}
