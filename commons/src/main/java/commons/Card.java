package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card {

    @Id
    private long id;
    private long listId;
    private String title;
    private long idx;
    private long boardId;

    /**
     * Empty constructor
     */
    public Card() {
    }

    /**
     * Constructor
     *
     * @param id the Card ID
     * @param listId this Card's List ID
     * @param title the Card's title
     * @param idx the position index of card in list
     * @param boardId the Card's Board ID
     */
    public Card(long id, long listId, String title, long idx, long boardId) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.idx = idx;
        this.boardId = boardId;
    }


    /**
     * Getter for Card ID
     *
     * @return the Card ID
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Card{" + "id='" + id + '\'' + ", cardListId='" + listId + '\'' + ", title='" +
            title + '\'' + ", nextCardId='" + idx + '\'' + ", boardId='" + boardId + '\'' + '}';
    }

    /**
     * {@inheritDoc}
     */
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
