package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;


@Entity
public class Board {
    String boardId;
    List<CardList> cardLists;

    public Board() {
    }

    public Board(String boardId, List<CardList> cardLists) {
        this.boardId = boardId;
        this.cardLists = cardLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(boardId, board.boardId) && Objects.equals(cardLists, board.cardLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, cardLists);
    }

    @Id
    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public List<CardList> getCardLists() {
        return cardLists;
    }

    public void setCardLists(List<CardList> cardLists) {
        this.cardLists = cardLists;
    }

}
