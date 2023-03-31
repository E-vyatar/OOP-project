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

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private final ServerUtils server;
    private Stage primaryStage;
    private BoardOverviewCtrl overviewCtrl;
    private Scene overview;
    private ConnectServerCtrl connectServerCtrl;
    private Scene connectServer;
    private ListOfBoardsCtrl listOfBoardsCtrl;
    private Scene listOfBoards;

    /**
     * This is and @Inject contractor and should not be called.
     *
     * @param server the ServerUtils for this app
     */
    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    /**
     * This method initializes MainCtrl. The roots of the views are used to create scenes.
     * It also starts showing the primary stage / the main window.
     * @param primaryStage the main window, this is used for ConnectServer and BoardOverview
     * @param overview a pair of the BoardOverviewCtrl and the root of the to-be scene
     * @param connectServerCtrl a pair of the connectServerCtrl and the root of the to-be scene.
     * @param listOfBoards a pair of the ListOfBoardsCtrl and the root of the to-be scene.
     */
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
     * Show the board overview
     * TODO: let BoardOverviewCtrl display the right board
     *
     * @param boardId the board for which to show the board overview
     */
    public void showOverview(long boardId) {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(overview);
        primaryStage.setX(150);
        primaryStage.setY(100);
        overviewCtrl.refresh(boardId);
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
        listOfBoardsCtrl.refresh();
        primaryStage.setTitle("List of boards");
        primaryStage.setScene(listOfBoards);
    }

    /**
     * Getter for the ServerUtil of this client
     *
     * @return The SeverUtils of this client
     */
    public ServerUtils getServer() {
        return server;
    }

}