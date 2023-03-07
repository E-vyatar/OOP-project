package commons;

public class Card {

    public long id;

    public long listID;
    public String title;

    public Card(long listID, String title) {
        this.listID = listID;
        this.title = title;
    }
}
