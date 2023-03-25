package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;

    @OneToMany(mappedBy = "boardId")
    @OrderColumn(name = "idx")
    private List<CardList> cardLists = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public Board() {
    }

    /**
     * Constructor (without id parameter)
     *  - ID will be auto generated by DB
     *
     * @param title the title of the board
     */
    public Board(String title) {
        this.title = title;
    }

    /**
     * Constructor (with id parameter)
     *
     * @param id the id of the board
     * @param title the title of the board
     */
    public Board(long id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * Getter for ID
     *
     * @return the id of the board
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for ID
     *
     * @param boardId the new id of the board
     */
    public void setId(long boardId) {
        this.id = boardId;
    }

    /**
     * Getter for Board Title
     *
     * @return the title of the board
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for board title
     *
     * @param title the new title of the board
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for cardList (list of all CardLists)
     *
     * @return the List of all CardLists on the board
     */
    public List<CardList> getCardLists() {
        return cardLists;
    }

    /**
     * Setter for cardList (list of all CardLists)
     *
     * @param cardLists the new cardList (list of all CardLists)
     */
    public void setCardLists(List<CardList> cardLists) {
        this.cardLists = cardLists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id == board.id && Objects.equals(cardLists, board.cardLists);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, cardLists);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Board{" +
            "id=" + id +
            ", cardLists=" + cardLists +
            '}';
    }
}

