package client.utils;

import client.scenes.BoardOverviewCtrl;
import commons.CardList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.util.List;

public class CardsUtils {


    private final BoardOverviewCtrl boardOverviewCtrl;

    @Inject
    public CardsUtils(BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
    }

    public void initializeListsDropDown(ComboBox<CardList> list) {
        List<CardList> cardsLists = boardOverviewCtrl.getAllLists();
        list.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(CardList item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
        list.setConverter(new StringConverter<>() {
            @Override
            public String toString(CardList cardList) {
                if (cardList != null) {
                    return cardList.getTitle();
                } else {
                    return "";
                }
            }

            @Override
            public CardList fromString(String string) {
                // not used, but must be implemented
                return null;
            }
        });
        list.getItems().clear();
        list.getItems().addAll(cardsLists);
    }

    public boolean fieldsNotEmpty(TextField cardTitle, ComboBox<CardList> list) {
        return !cardTitle.getText().isBlank() && !list.getSelectionModel().isEmpty();
    }

    public void markFields(TextField cardTitle, ComboBox<CardList> list) {
        if (cardTitle.getText().isBlank()) {
            cardTitle.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        if (list.getSelectionModel().isEmpty()) {
            list.setStyle("-fx-border-color: red ; -fx-border-width: 2px");
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            cardTitle.setStyle("");
            list.setStyle("");
        }));
        timeline.play();
    }
}
