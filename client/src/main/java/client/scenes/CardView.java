package client.scenes;

import commons.Card;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EmbeddedCardView extends Pane {

    private final EmbeddedCardViewCtrl controller;

    public EmbeddedCardView(EmbeddedCardViewCtrl controller) {
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
