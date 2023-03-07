package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    public String name;

    public User(String name) {
        this.name = name;
    }
    public User() {

    }
}
