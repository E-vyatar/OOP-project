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

import commons.*;
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

    //=========================================================
    // This is temporary in order to demonstrate functionality:
    //     - It will be merged into main project later.
    private Stage secondaryStage;
    private ListOverviewCtrl listOverviewCtrl;
    private Scene listOverview;

    private RenameListPopupCtrl renameListPopupCtrl;
    private Stage renameListPopup;
    //=========================================================

    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> overview,
                           Pair<CardPopupCtrl, Parent> cardPopup,
                           Pair<ListOverviewCtrl, Parent> listOverview,
                           Pair<RenameListPopupCtrl, Parent> renameListPopup) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.cardPopupCtrlCtrl = cardPopup.getKey();
        this.cardPopup = new Stage();
        this.cardPopup.initModality(Modality.WINDOW_MODAL);
        this.cardPopup.setMinWidth(240.0);
        this.cardPopup.setMinHeight(200.0);
        this.cardPopup.setScene(new Scene(cardPopup.getValue()));

        showOverview();
        primaryStage.show();

        this.secondaryStage = new Stage();
        this.listOverviewCtrl = listOverview.getKey();
        this.listOverview = new Scene(listOverview.getValue(), 200, 200);

        this.renameListPopupCtrl = renameListPopup.getKey();
        this.renameListPopup = new Stage();
        this.renameListPopup.setX(this.renameListPopup.getX() + 100);
        this.renameListPopup.initModality(Modality.APPLICATION_MODAL);
        this.renameListPopup.setScene(new Scene(renameListPopup.getValue(), 300, 200));

        showExampleListOverview();
        secondaryStage.show();
    }

    private void showExampleListOverview() {
        secondaryStage.setTitle("Example: rename list");
        secondaryStage.setScene(listOverview);
        listOverviewCtrl.refresh();
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

    public void showRenameList(CardList cardList) {
        renameListPopupCtrl.setCardList(cardList);
        renameListPopup.show();
    }

    public void hideRenameListPopup() {
        renameListPopup.hide();
    }
}