package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;


@Entity
public class Board {
    @Id
    long boardId;

    @OneToMany
    List<CardList> cardLists;

    public Board() {
    }

    public Board(long boardId, List<CardList> cardLists) {
        this.boardId = boardId;
        this.cardLists = cardLists;
    }


    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public List<CardList> getCardLists() {
        return cardLists;
    }

    public void setCardLists(List<CardList> cardLists) {
        this.cardLists = cardLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return boardId == board.boardId && Objects.equals(cardLists, board.cardLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, cardLists);
    }
}
