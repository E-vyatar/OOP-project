package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private long idx;
    private long boardId;

    /**
     * This is an empty constructor. It doesn't initialize any values.
     */
    public CardList() {
    }

    /**
     * Constructor without 'id' parameter (sets id = -1 to avoid errors)
     * - ID will be generated automatically by the database
     *
     * @param title   the title
     * @param boardId the board's id
     * @param idx the index for the order of cardlists in the board
     */
    public CardList(String title, long boardId, long idx) {
        this.title = title;
        this.boardId = boardId;
        this.idx = idx;
    }

    /**
     * Constructor with 'id' parameter
     *
     * @param id the CardList's id
     * @param title   the title
     * @param boardId the board's id
     * @param idx the index for the order of cardlists in the board
     */
    public CardList(long id, String title, long idx, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
    }

    /**
     * Get the index of the CardList that specifies where in the board it is
     * with respect to other CardLists
     * @return the index
     */
    public long getIdx() {
        return idx;
    }

    /**
     * Get the index of the CardList that specifies where in the board it is
     * with respect to other CardLists
     * @param idx the new index
     */
    public void setIdx(long idx) {
        this.idx = idx;
    }

    /**
     * Change the title of the CardList to the new title
     * @param title the new title
     */
    public void renameCardList(String title) {
        this.title = title;
    }

    /**
     * Get the id of the CardList
     * @return the CardList's id
     */
    @Id
    public long getId() {
        return id;
    }

    /**
     * Set the id of the CardList
     * @param cardListId the new CardList's id
     */
    public void setId(long cardListId) {
        this.id = cardListId;
    }

    /**
     * Get the title of the CardList
     * @return the CardList's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the CardList
     * @param cardListTitle the new title of the CardList
     */
    public void setTitle(String cardListTitle) {
        this.title = cardListTitle;
    }

    /**
     * Get the id of the Board the CardList is in
     * @return the Board's id
     */
    public long getBoardId() {
        return boardId;
    }

    /**
     * Set the id of the board the CardList is in
     * @param boardId the new Board's id
     */
    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    /**
     * Check for object equality
     * @param o the object to compare it with
     * @return whether they're equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardList)) return false;
        CardList cardList = (CardList) o;
        return id == cardList.id && idx == cardList.idx && boardId == cardList.boardId &&
                Objects.equals(title, cardList.title);
    }

    /**
     * Generate a hashcode for the CardList
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, idx, boardId);
    }


}
