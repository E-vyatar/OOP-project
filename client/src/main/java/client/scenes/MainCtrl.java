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
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private EditCardCtrl editCardCtrl;
    private Scene editCard;


    public void initialize(Stage primaryStage,
            Pair<BoardOverviewCtrl, Parent> overview,
            Pair<CardPopupCtrl, Parent> cardPopup,
            Pair<AddCardCtrl, Parent> addCard,
            Pair<EditCardCtrl, Parent> editCard
        ) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());
        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.cardPopupCtrlCtrl = cardPopup.getKey();
        this.cardPopup = new Stage();
        this.cardPopup.initModality(Modality.WINDOW_MODAL);
        this.cardPopup.setMinWidth(240.0);
        this.cardPopup.setMinHeight(200.0);
        this.cardPopup.setScene(new Scene(cardPopup.getValue()));

        showOverview();

        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Empty Scene <overview>");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showCard(Card card) {
        cardPopupCtrlCtrl.setCard(card);
        cardPopup.show();
    }
    public void hideCard() {
        cardPopup.hide();
    }
    public void showAddCard () {
        primaryStage.setTitle("Empty Scene <addCard>");
        primaryStage.setScene(addCard);
        addCardCtrl.refresh();
    }

    public void showEditCard (String title) {
        primaryStage.setTitle("Empty Scene <editCard>");
        primaryStage.setScene(editCard);
        editCardCtrl.setTitle(title);
        editCardCtrl.refresh();
    }

}