package server.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Card implements Serializable {

    private final String title;
    private final String listId;
    private final String boardId;
    private final String nextCardId;
    @Id
    private String id;

    public Card(@JsonProperty String title, @JsonProperty String boardId, @JsonProperty String listId, @JsonProperty String nextCardId) {
        super();
        this.title = title;
        this.listId = listId;
        this.boardId = boardId;
        this.nextCardId = nextCardId;
    }


    @Override
    public String toString() {
        return "Card{" +
                "title='" + title + '\'' +
                ", listId='" + listId + '\'' +
                ", boardId='" + boardId + '\'' +
                ", nextCardId='" + nextCardId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getListId() {
        return listId;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getNextCardId() {
        return nextCardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
