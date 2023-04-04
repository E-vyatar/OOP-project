package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import commons.Board;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.List;

/**
 * The controller for the list of boards.
 * The list of boards is the interface that allows you
 * to select which board you want to view. It also has
 * buttons to disconnect from the server, add a pre-existing
 * board and to create a new board.
 */
public class ListOfBoardsCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final SocketsUtils sockets;
    private final ClientConfig config;
    @FXML
    private ListView<Board> boards;

    @FXML
    private HBox userButtons;
    @FXML
    private HBox adminButtons;

    /**
     * This constructs an instance of ListOfBoards.
     *
     * @param mainCtrl    the main controller
     * @param server the server utils - used to load list of boards
     * @param sockets the socket utils - used to disconnect connection
     * @param clientConfig the client configuration - used to get list of boards to retrieve
     */
    @Inject
    public ListOfBoardsCtrl(MainCtrl mainCtrl,
                            ServerUtils server,
                            SocketsUtils sockets,
                            ClientConfig clientConfig) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.sockets = sockets;
        this.config = clientConfig;
    }


    /**
     * This method initializes the ListView.
     */
    public void initialize() {
        this.boards.setCellFactory(param -> {
            return new BoardCell();
        });
    }
    /**
     * Refresh the controller.
     * This loads data from the backend and sets the listView.
     */
    public void refresh() {
        List<Board> boards;
        if (isAdmin()) {
            boards = server.getAllBoards();
        } else {
            boards = server.getAllBoards(config.getIds(server.getHostname()));
        }

        ObservableList<Board> data = FXCollections.observableList(boards);
        this.boards.setItems(data);

        if (isAdmin()) {
            adminButtons.setManaged(true);
            adminButtons.setVisible(true);
            userButtons.setManaged(false);
            userButtons.setVisible(false);
        } else {
            adminButtons.setManaged(false);
            adminButtons.setVisible(false);
            userButtons.setManaged(true);
            userButtons.setVisible(true);
        }
    }

    /**
     * Disconnect from server. When called, the Stompsession is ended
     * and scene is set up back to ConnectServerCtrl
     *
     * @param mouseEvent the mouse event - unused
     */
    public void disconnect(MouseEvent mouseEvent) {
        sockets.disconnect();
        System.out.println("The client has been disconnected");

        mainCtrl.showConnect();
    }
    /**
     * Remove a board
     *
     */
    public void removeBoard() {
        Board board = this.boards.getSelectionModel().getSelectedItem();
        if (board == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a board to remove from your list");
            alert.show();
        } else {
            config.removeBoard(server.getHostname(), board.getId());
            this.boards.getSelectionModel().clearSelection();
            this.boards.getItems().remove(board);
        }
    }
    /**
     * delete a board
     *
     */
    @FXML
    private void deleteBoard() {
        Board board = this.boards.getSelectionModel().getSelectedItem();
        if (board == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a board to delete from your list");
            alert.show();
        } else {
            deleteBoard(board);
            this.boards.getSelectionModel().clearSelection();
            this.boards.getItems().remove(board);
        }
    }

    /**
     * Delete a board.
     * This will delete the board and remove it from your config.
     * @param board
     */
    public void deleteBoard(Board board) {
        long boardId = board.getId();
        server.deleteBoard(boardId);
        config.removeBoard(server.getHostname(), boardId);
    }
    /**
     * Add a new board
     *
     */
    @FXML
    public void addBoard() {
        mainCtrl.showAddBoard();
    }

    /**
     * Go to the interface to create a new board
     */
    @FXML
    public void newBoard() {
        mainCtrl.showCreateBoard();
    }

    /**
     * Open a board
     */
    @FXML
    public void openBoard() {
        Board board = this.boards.getSelectionModel().getSelectedItem();
        if (board == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a board to open");
            alert.show();
        } else {
            long boardId = board.getId();
            mainCtrl.showOverview(boardId);
        }
    }

    static class BoardCell extends ListCell<Board> {

        @Override
        public void updateItem(Board item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setGraphic(null);
            } else {
                Label label = new Label(item.getTitle());
                this.setGraphic(label);
                this.getStyleClass().add("board");
            }
        }
    }

    /**
     * Check if the user is admin
     */
    private boolean isAdmin() {
        return server.hasPassword();
    }
}
