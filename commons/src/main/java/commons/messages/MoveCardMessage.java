package commons.messages;

import java.io.Serializable;
import java.util.Objects;

public class MoveCardMessage implements Serializable {
    private long cardId;
    private long newListId;
    private long newIndex;

    /**
     * Constructor
     */
    public MoveCardMessage() {
    }


    /**
     * Constructor
     * @param cardId the id of the card
     * @param newListId the id of the new list
     * @param newIndex the new index of the card
     */
    public MoveCardMessage(long cardId, long newListId, long newIndex) {
        this.cardId = cardId;
        this.newListId = newListId;
        this.newIndex = newIndex;
    }

    /**
     * Returns the id of the card
     * @return the id of the card
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * Returns the id of the new list
     * @return the id of the new list
     */
    public long getNewListId() {
        return newListId;
    }

    /**
     * Returns the id of the board
     * @return the id of the board
     */
    public long getNewIndex() {
        return newIndex;
    }

    /**
     * Sets the id of the card
     * @param cardId the id of the card
     */
    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    /**
     * Sets the id of the new list
     * @param newListId the id of the new list
     */
    public void setNewListId(long newListId) {
        this.newListId = newListId;
    }

    /**
     * Sets the new index of the card
     * @param newIndex the new index of the card
     */
    public void setNewIndex(long newIndex) {
        this.newIndex = newIndex;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveCardMessage that = (MoveCardMessage) o;
        return cardId == that.cardId && newListId == that.newListId && newIndex == that.newIndex;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardId, newListId, newIndex);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "MoveCardMessage{" +
                "cardId=" + cardId +
                ", newListId=" + newListId +
                ", newIndex=" + newIndex +
                '}';
    }
}
