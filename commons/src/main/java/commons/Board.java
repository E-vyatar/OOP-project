package commons;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String name;
    @OneToMany
    List<CardList> cardLists;

    public Board() {
    }

    public Board(long id, String name, List<CardList> cardLists) {
        this.id = id;
        this.name = name;
        this.cardLists = cardLists;
    }

    /**
     * Constructor without 'id' parameter (sets id = -1 to avoid errors)
     * - The id would be generated automatically by the database.
     *
     * @param cardLists the list of all the CardLists in the board
     */
    public Board(List<CardList> cardLists) {
        this.id = -1;
        this.cardLists = cardLists;
    }


    public long getId() {
        return id;
    }

    public void setId(long boardId) {
        this.id = boardId;
    }

    public List<CardList> getCardLists() {
        return cardLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id == board.id && Objects.equals(cardLists, board.cardLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardLists);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

