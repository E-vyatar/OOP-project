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

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new FXConfig());
    private static final FXMLInitializer FXML = new FXMLInitializer(INJECTOR);

    /**
     * The main method. This starts the client.
     * @param args the arguments passed to the program.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * This method is called by JavaFX and starts the program.
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        var cardPopup = FXML.load(CardPopupCtrl.class,
                "client", "scenes", "CardPopup.fxml");
        var renameListPopup = FXML.load(RenameListPopupCtrl.class,
                "client", "scenes", "RenameListPopup.fxml");
        var addCard = FXML.load(AddCardCtrl.class,
                "client", "scenes", "AddCard.fxml");

        var overview = FXML.load(BoardOverviewCtrl.class,
                "client", "scenes", "BoardOverview.fxml");


        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        overview.getKey().initialize(cardPopup, addCard, renameListPopup);

        var connectServerCtrl = FXML.load(ConnectServerCtrl.class,
                "client", "scenes", "ConnectServer.fxml");

        mainCtrl.initialize(primaryStage, overview, connectServerCtrl);
    }
}