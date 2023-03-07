package commons;

import javax.persistence.Entity;

@Entity
public class BoardList {

    public long id;

    public String title;


    public BoardList(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
