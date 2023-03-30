package commons.messages;

import java.io.Serializable;
import java.util.Objects;

public class MoveCardMessage implements Serializable {
    private long cardId;
    private long newListId;
    private long newIndex;

    public MoveCardMessage() {
    }


    public MoveCardMessage(long cardId, long newListId, long newIndex) {
        this.cardId = cardId;
        this.newListId = newListId;
        this.newIndex = newIndex;
    }

    public long getCardId() {
        return cardId;
    }

    public long getNewListId() {
        return newListId;
    }

    public long getNewIndex() {
        return newIndex;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public void setNewListId(long newListId) {
        this.newListId = newListId;
    }

    public void setNewIndex(long newIndex) {
        this.newIndex = newIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveCardMessage that = (MoveCardMessage) o;
        return cardId == that.cardId && newListId == that.newListId && newIndex == that.newIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, newListId, newIndex);
    }

    @Override
    public String toString() {
        return "MoveCardMessage{" +
            "cardId=" + cardId +
            ", newListId=" + newListId +
            ", newIndex=" + newIndex +
            '}';
    }
}
