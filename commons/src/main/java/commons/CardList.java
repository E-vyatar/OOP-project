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
    long id;
    String title;
    long idx;
    long boardId;

    public CardList() {
    }

    /**
     * Constructor without 'id' parameter (sets id = -1 to avoid errors)
     * - ID will be generated automatically by the database
     *
     * @param title   the title
     * @param boardId the board's id
     */
    public CardList(String title, long boardId, long idx) {
        this.title = title;
        this.boardId = boardId;
        this.idx = idx;
    }

    public CardList(long id, String title, long idx, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
        this.idx = idx;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public void renameCardList(String newName) {
        this.title = newName;
    }

    @Id
    public long getId() {
        return id;
    }

    public void setId(long cardListId) {
        this.id = cardListId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String cardListTitle) {
        this.title = cardListTitle;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardList)) return false;
        CardList cardList = (CardList) o;
        return id == cardList.id && idx == cardList.idx && boardId == cardList.boardId && Objects.equals(title, cardList.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, idx, boardId);
    }


}
