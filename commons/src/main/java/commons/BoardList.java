package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BoardList {

    public long id;

    public String title;

    private List<Card> cards;

    public BoardList(long id, String title) {
        this.id = id;
        this.title = title;
        this.cards = new ArrayList<>();

        // Temporary hardcoding
            for (int i = 0; i < 4; i++){
                this.cards.add(new Card("Card Title" + this.id + "." + i));
            }
    }
    public List<Card> getCards() {
        return this.cards;
    }
}
