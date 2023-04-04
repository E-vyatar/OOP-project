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

    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoard;

    private AddBoardCtrl addBoardCtrl;
    private Scene addBoard;

    //=========================================================

    /**
     * This method initializes MainCtrl. The roots of the views are used to create scenes.
     * It also starts showing the primary stage / the main window.
     *
     * @param primaryStage      the main window, this is used for ConnectServer and BoardOverview
     * @param overview          a pair of the BoardOverviewCtrl and the root of the to-be scene
     * @param connectServerCtrl a pair of the connectServerCtrl and the root of the to-be scene.
     * @param listOfBoards a pair of the ListOfBoardsCtrl and the root of the to-be scene.
     * @param createBoard a pair of the CreateBoardCtrl and the root of the to-be scene.
     * @param addBoard a pair of the AddBoardCtrl and the root of the to-be scene.
     */
    public void initialize(Stage primaryStage,
                           Pair<BoardOverviewCtrl, Parent> overview,
                           Pair<ConnectServerCtrl, Parent> connectServerCtrl,
                           Pair<ListOfBoardsCtrl, Parent> listOfBoards,
                           Pair<CreateBoardCtrl, Parent> createBoard,
                           Pair<AddBoardCtrl, Parent> addBoard) {

        this.primaryStage = primaryStage;

        this.connectServerCtrl = connectServerCtrl.getKey();
        this.connectServer = new Scene(connectServerCtrl.getValue());

        this.overview = new Scene(overview.getValue());
        this.overviewCtrl = overview.getKey();

        this.listOfBoards = new Scene(listOfBoards.getValue());
        this.listOfBoardsCtrl = listOfBoards.getKey();

        this.createBoard = new Scene(createBoard.getValue());
        this.createBoardCtrl = createBoard.getKey();

        this.addBoard = new Scene(addBoard.getValue());
        this.addBoardCtrl = addBoard.getKey();

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

    /**
     * Show the list with all known boards.
     */
    public void showListOfBoards() {
        listOfBoardsCtrl.refresh();
        primaryStage.setTitle("List of boards");
        primaryStage.setScene(listOfBoards);
    }

    /**

     * Shows the UI to create a new board
     */
    public void showCreateBoard() {
        createBoardCtrl.clear();
        primaryStage.setTitle("Create board");
        primaryStage.setScene(createBoard);
    }

    /**

     * Shows the UI to add a board, so it's visible in your list.
     */
    public void showAddBoard() {
        addBoardCtrl.clear();
        primaryStage.setTitle("Add board");
        primaryStage.setScene(addBoard);
    }
}