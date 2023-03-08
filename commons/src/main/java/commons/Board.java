package commons;

import java.util.List;
import java.util.Objects;

public class Board {
    String boardId;
    List<CardList> cardLists;

    public Board(String boardId, List<CardList> cardLists) {
        this.boardId = boardId;
        this.cardLists = cardLists;
    }

    public String getBoardId() {
        return boardId;
    }

    public List<CardList> getCardLists() {
        return cardLists;
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
}
