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

    private ListOfBoardsCtrl listOfBoardsCtrl;
    private Scene listOfBoards;
    //=========================================================

    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> overview,
                           Pair<ConnectServerCtrl, Parent> connectServerCtrl,
                           Pair<ListOfBoardsCtrl, Parent> listOfBoards) {

        this.primaryStage = primaryStage;

        this.connectServerCtrl = connectServerCtrl.getKey();
        this.connectServer = new Scene(connectServerCtrl.getValue());

        this.overview = new Scene(overview.getValue());
        this.overviewCtrl = overview.getKey();

        this.listOfBoards = new Scene(listOfBoards.getValue());
        this.listOfBoardsCtrl = listOfBoards.getKey();

        showconnect();
        this.primaryStage.show();
    }

    public void showconnect() {
        primaryStage.setTitle("Connect");
        primaryStage.setScene(connectServer);

    }

    /**
     * Show the board overview
     * TODO: let BoardOverviewCtrl display the right board
     * @param boardId the board for which to show the board overview
     */
    public void showOverview(long boardId) {
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

    /**
     * Show the list with all known boards.
     */
    public void showListOfBoards() {
        primaryStage.setTitle("List of boards");
        primaryStage.setScene(listOfBoards);
    }
}