package commons;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
     * @param cardLists the list of CardLists in the board
     */
    public Board(long id, List<CardList> cardLists) {
        this.id = id;
        this.cardLists = cardLists;
    }

    /**
     * Constructor without 'id' parameter (sets id = -1 to avoid errors)
     *  - The id would be generated automatically by the database.
     *
     * @param cardLists the list of all the CardLists in the board
     */
    public Board(List<CardList> cardLists) {
        this.id = -1;
        this.cardLists = cardLists;
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
     * Setter for cardLists (List of CardLists on this board)
     *
     * @param cardLists the new List of CardLists
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

}

