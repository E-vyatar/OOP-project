package client.scenes;

import commons.Card;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class CardView extends Pane {

    private Card card;
    private TextField cardTitle;

    public CardView(Card card) {
        this.card = card;
        createView();
    }
    private void createView() {
        Label label = new Label();
        label.setText(card.title);
        this.getChildren().add(label);

        this.setMinHeight(150.0);
    }

}
