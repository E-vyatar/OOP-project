package client.scenes;

import commons.Card;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class CardView extends Pane {

    private final CardViewCtrl controller;

    public CardView(CardViewCtrl controller) {
        this.controller = controller;
        createView();
    }
    private void createView() {
        Card card = controller.getCard();

        Label label = new Label();
        label.setText(card.title);
        this.getChildren().add(label);

        this.setMinHeight(150.0);

        this.setOnMouseClicked(this.controller);
    }

}
