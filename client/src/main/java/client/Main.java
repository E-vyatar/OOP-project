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
package client;

import client.scenes.CardPopupCtrl;
import client.scenes.AddCardCtrl;
import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.scenes.BoardOverviewCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new FXConfig());
    private static final FXMLInitializer FXML = new FXMLInitializer(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        var overview = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var cardPopup = FXML.load(CardPopupCtrl.class, "client", "scenes", "CardPopup.fxml");


        var addCard = FXML.load(AddCardCtrl.class, "client", "scenes", "AddCard.fxml");
        var editCard = FXML.load(EditCardCtrl.class, "client", "scenes", "EditCard.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(primaryStage, overview, cardPopup, addCard, editCard);
    }
}