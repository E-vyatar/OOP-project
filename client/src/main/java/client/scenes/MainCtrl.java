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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private BoardOverviewCtrl overviewCtrl;
    private Scene overview;

    private ConnectServerCtrl connectServerCtrl;

    private Scene connectServer;
    //=========================================================

    /**
     * This method initializes MainCtrl. The roots of the views are used to create scenes.
     * It also starts showing the primary stage / the main window.
     * @param primaryStage the main window, this is used for ConnectServer and BoardOverview
     * @param overview a pair of the BoardOverviewCtrl and the root of the to-be scene
     * @param connectServerCtrl a pair of the connectServerCtrl and the root of the to-be scene.
     */
    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> overview,
                           Pair<ConnectServerCtrl, Parent> connectServerCtrl) {

        this.primaryStage = primaryStage;

        this.connectServerCtrl = connectServerCtrl.getKey();
        this.connectServer = new Scene(connectServerCtrl.getValue());

        this.overview = new Scene(overview.getValue());
        this.overviewCtrl = overview.getKey();

        showConnect();
        this.primaryStage.show();
    }

    /**
     * This sets the main window to connectServer scene.
     */
    public void showConnect() {
        primaryStage.setTitle("Connect");
        primaryStage.setScene(connectServer);
    }

    /**
     * This sets the main window to the board overview scene.
     */
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

}