package client.utils;

import client.scenes.BoardOverviewCtrl;
import commons.CardList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.util.StringConverter;

import javax.inject.Inject;

public class CardsUtils {


    private final BoardOverviewCtrl boardOverviewCtrl;

    /**
     * constructor
     *
     * @param boardOverviewCtrl reference to board controller for lists references
     */
    @Inject
    public CardsUtils(BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    /**
     * set a dropdown with all current lists in the board, displaying the lists' names
     *
     * @param list ChoiceBox to put the lists in
     */
    public void initializeListsDropDown(ChoiceBox<CardList> list) {
        StringConverter<CardList> stringConverter = new StringConverter<>() {
            @Override
            public String toString(CardList object) {
                if (object != null) {
                    return object.getTitle();
                }
                return "";
            }

            @Override
            public CardList fromString(String string) {
                return null;
            }
        };

        list.setConverter(stringConverter);

        list.getItems().clear();
        list.getItems().addAll(
                FXCollections.observableList(boardOverviewCtrl.getAllLists())
        );
    }

    /**
     * check fields' values are not empty
     *
     * @param cardTitle text field to check the value of
     * @param list      choice box to check the value of
     * @return true if every field has a value, false otherwise
     */
    public boolean fieldsNotEmpty(TextField cardTitle, ChoiceBox<CardList> list) {
        boolean res = true;
        if (list != null) {
            res = !list.getSelectionModel().isEmpty();
        }
        return !cardTitle.getText().isBlank() && res;
    }

    /**
     * change fields' border colour to red for 0.5 second
     *
     * @param cardTitle text field to change the border colour of
     * @param list      choice box to change the border colour of
     */
    public void markFields(TextField cardTitle, ChoiceBox<CardList> list) {
        if (cardTitle.getText().isBlank()) {
            cardTitle.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        if (list != null && list.getSelectionModel().isEmpty()) {
            list.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            cardTitle.setStyle("");
            if (list != null) {
                list.setStyle("");
            }
        }));
        timeline.play();
    }
}
