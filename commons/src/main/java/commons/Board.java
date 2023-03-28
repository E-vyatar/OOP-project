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
    @OneToMany
    private List<CardList> cardLists;

    /**
     * Empty constructor.
     */
    public Board() {
    }

    /**
     * Constructor.
     *
     * @param id the id of the board
     * @param title the name of the board
     * @param cardLists the list of CardLists in the board
     */
    public Board(long id, String title, List<CardList> cardLists) {
        this.id = id;
        this.title = title;
        this.cardLists = cardLists;
    }

    /**
     * Constructor without 'id' parameter (sets id = -1 to avoid errors)
     * - The id would be generated automatically by the database.
     *
     * @param title the name of the board
     */
    public Board(String title) {
        this.title = title;
        this.cardLists = new ArrayList<>();
    }


    /**
     * Getter for board ID
     *
     * @return the id of the board
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for board ID
     *
     * @param boardId the new board ID
     */
    public void setId(long boardId) {
        this.id = boardId;
    }

    /**
     * Getter for List of CardLists on this board.
     *
     * @return the List of CardLists on the board
     */
    public List<CardList> getCardLists() {
        return cardLists;
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
     * Get the name of a board
     * @return the board's name
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the name of the board
     * @param title the board's name
     */
    public void setTitle(String title) {
        this.title = title;
    }
}

