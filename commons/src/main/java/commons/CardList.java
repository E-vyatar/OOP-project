package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "list_id_seq")
    @SequenceGenerator(name = "list_id_seq", initialValue = 10, allocationSize = 1)
    private long id;
    private long boardId;
    private String title;
    private long idx;

    @OneToMany(mappedBy = "listId", cascade = CascadeType.ALL)
    @OrderColumn(name = "idx")
    private List<Card> cards = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public CardList() {
    }

    /**
     * Constructor (without 'id' parameter)
     * - ID will be generated automatically by the database
     *
     * @param title   the title
     * @param boardId the board's id
     * @param idx     the position index of this CardList in the board
     */
    public CardList(String title, long boardId, long idx) {
        this.title = title;
        this.boardId = boardId;
        this.idx = idx;
    }

    /**
     * Constructor with 'id' parameter
     *
     * @param id      the CardList's id
     * @param title   the title
     * @param boardId the board's id
     * @param idx     the index for the order of cardlists in the board
     */
    public CardList(long id, String title, long idx, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
        this.idx = idx;
    }

    /**
     * Getter for Id
     *
     * @return the id of this CardList
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param cardListId the new id of this CardList
     */
    public void setId(long cardListId) {
        this.id = cardListId;
    }

    /**
     * Getter for the id of the Board the CardList is in
     *
     * @return the Board's id
     */
    public long getBoardId() {
        return boardId;
    }

    /**
     * Setter for board ID of this CardList
     *
     * @param boardId the new board ID of this CardList
     */
    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    /**
     * Getter for title
     *
     * @return the title of this CardList
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     *
     * @param newTitle the new title of the id
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Getter for position index.
     *
     * @return the position index of this CardList in the board
     */
    public long getIdx() {
        return idx;
    }

    /**
     * Setter for position index.
     *
     * @param idx the new position of the CardList in the board
     */
    public void setIdx(long idx) {
        this.idx = idx;
    }

    /**
     * Get the cards inside this CardList
     *
     * @return a list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Set the cards inside this CardList
     *
     * @param cards the list of cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Checks if the ID of the CardLists match
     *
     * @param o the Object to compare to
     * @return whether they're equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList cardList = (CardList) o;
        return id == cardList.id;
    }

    /**
     * Generate a hashcode for the CardList
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, boardId, title, idx, cards);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CardList{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", idx=" + idx +
            ", boardId=" + boardId +
            ", cards=" + cards +
            '}';
    }

}
