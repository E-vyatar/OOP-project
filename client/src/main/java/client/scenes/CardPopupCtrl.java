package client.scenes;

import client.utils.CardsUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import javax.inject.Inject;

public class CardPopupCtrl {

    private final ServerUtils server;
    private final CardsUtils cardsUtils;
    private final MainCtrl mainCtrl;

    public Card card;

    @FXML
    private TextField cardTitle;
    @FXML
    private ComboBox<CardList> list;
    @FXML
    private TextArea cardDescription;

    @Inject
    public CardPopupCtrl(ServerUtils server, CardsUtils cardsUtils, MainCtrl mainCtrl, BoardOverviewCtrl boardOverviewCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.cardsUtils = cardsUtils;
    }

    public void setCard(Card card) {
        this.card = card;
        createView();
    }

    private void createView() {
        cardTitle.setText(card.getTitle());

        cardsUtils.initializeListsDropDown(list);
//        List<CardList> cardsLists = boardOverviewCtrl.getAllLists();
//        list.setCellFactory(listView -> new ListCell<>() {
//            @Override
//            protected void updateItem(CardList item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(item.getTitle());
//                }
//            }
//        });
//        list.setConverter(new StringConverter<>() {
//            @Override
//            public String toString(CardList cardList) {
//                if (cardList != null) {
//                    return cardList.getTitle();
//                } else {
//                    return "";
//                }
//            }
//
//            @Override
//            public CardList fromString(String string) {
//                // not used, but must be implemented
//                return null;
//            }
//        });
//        list.getItems().clear();
//        list.getItems().addAll(cardsLists);
        list.getSelectionModel().select((int) card.getListId());

        cardDescription.setText("Here there will be a description.");

    }

    @FXML
    private void close() {
        mainCtrl.hideCard();
    }

    @FXML
    private void save() {
        if (cardsUtils.fieldsNotEmpty(cardTitle, list)) {
            try {
                Card updatedCard = new Card(list.getValue().getId(), cardTitle.getText(), card.getIdx(), card.getBoardId());
                server.updateCard(updatedCard);
                close();
            } catch (WebApplicationException e) {

                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            mainCtrl.showOverview();
        } else {
            cardsUtils.markFields(cardTitle, list);
        }
    }
}
