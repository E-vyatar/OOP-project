package commons.messages;

import java.io.Serializable;
import java.util.Objects;

public class MoveCardMessage implements Serializable {
    private long cardId;
    private long newListId;
    private long oldListId;
    private long newIndex;
    private boolean moved = false;

    /**
     * Empty constructor
     */
    public MoveCardMessage() {}

    /**
     * Constructor
     * @param cardId the id of the card
     * @param newListId the id of the new list
     * @param oldListId the id of the old list
     * @param newIndex the new index of the card
     */
    public MoveCardMessage(long cardId, long newListId, long oldListId, long newIndex) {
        this.cardId = cardId;
        this.newListId = newListId;
        this.oldListId = oldListId;
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
     * Getter for the old list id
     *
     * @return id of the old list
     */
    public long getOldListId() {
        return oldListId;
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
     * Setter for the old list id
     *
     * @param oldListId the old list id
     */
    public void setOldListId(long oldListId) {
        this.oldListId = oldListId;
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
        return sameValues(that);
    }

    /**
     * Used to reduce cyclomatic complexity of equals method
     *
     * @param that object to compare to
     * @return true if same values of properties
     */
    private boolean sameValues(MoveCardMessage that) {
        return cardId == that.cardId
            && newListId == that.newListId
            && oldListId == that.oldListId
            && newIndex == that.newIndex;
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

    /**
     * Getter for moved
     *
     * @return checks the return value of movement from server
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Setter for moved
     *
     * @param b the new value of server confirmation
     */
    public void setMoved(boolean b) {
        moved = b;
    }
}
