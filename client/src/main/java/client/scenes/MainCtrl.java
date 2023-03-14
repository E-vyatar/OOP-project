/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Card;
import commons.CardList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private BoardOverviewCtrl overviewCtrl;
    private Scene overview;

    private CardPopupCtrl cardPopupCtrlCtrl;
    private Stage cardPopup;

    private AddCardCtrl addCardCtrl;
    private Scene addCard;

    private RenameListPopupCtrl renameListPopupCtrl;
    private Stage renameListPopup;
    private ConnectServerCtrl connectServerCtrl;

    private Scene connectServer;
    //=========================================================

    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> overview,
                           Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup, Pair<ConnectServerCtrl, Parent> connectServerCtrl) {

        this.primaryStage = primaryStage;

        this.connectServerCtrl = connectServerCtrl.getKey();
        this.connectServer = new Scene(connectServerCtrl.getValue());

        this.overview = new Scene(overview.getValue());
        this.overviewCtrl = overview.getKey();

        this.cardPopupCtrlCtrl = cardPopup.getKey();
        this.cardPopup = new Stage();
        this.cardPopup.initModality(Modality.WINDOW_MODAL);
        this.cardPopup.setMinWidth(240.0);
        this.cardPopup.setMinHeight(200.0);
        this.cardPopup.setScene(new Scene(cardPopup.getValue()));

        this.renameListPopupCtrl = renameListPopup.getKey();
        this.renameListPopup = new Stage();
        this.renameListPopup.setX(this.renameListPopup.getX() + 100);
        this.renameListPopup.initModality(Modality.APPLICATION_MODAL);
        this.renameListPopup.setScene(new Scene(renameListPopup.getValue(), 300, 200));

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        showconnect();
        this.primaryStage.show();
    }

    public void showconnect() {
        primaryStage.setTitle("Connect");
        primaryStage.setScene(connectServer);

    }

    public void showOverview() {
        primaryStage.setTitle("Empty Scene <overview>");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }
//TODO solve the connection later
//    public void checkConnection() throws UnknownHostException {
//        if(connectServerCtrl.connect()){
//            showOverview();
//        }
//    }

    public void showCard(Card card) {
        cardPopupCtrlCtrl.setCard(card);
        cardPopup.show();
    }

    public void hideCard() {
        cardPopup.hide();
    }

    /**
     * Open a new window with "AddCard" scene
     */
    public void showAddCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle("Add new Task");
        cardWindow.setScene(addCard);
        addCard.setOnKeyPressed(event -> {
            addCardCtrl.keyPressed(event);
        });
        cardWindow.show();
    }

    public void showRenameList(CardList cardList) {
        renameListPopupCtrl.setCardList(cardList);
        renameListPopup.show();
    }

    public void hideRenameListPopup() {
        renameListPopup.hide();
    }

}