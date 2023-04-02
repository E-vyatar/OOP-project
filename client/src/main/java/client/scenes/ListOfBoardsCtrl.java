package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import commons.Board;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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

    private boolean isAdmin;

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
     * Refresh the controller.
     * This loads data from the backend and sets the listView.
     */
    public void refresh() {
        List<Board> boards;
        if (server.hasPassword()) {
            boards = server.getAllBoards();
        } else {
            boards = server.getAllBoards(config.getIds(server.getHostname()));
        }

        ObservableList<Board> data = FXCollections.observableList(boards);
        this.boards.setItems(data);
        this.boards.setCellFactory(param -> {
            /*BoardCellCtrl boardCellCtrl = new BoardCellCtrl();
            return boardCellCtrl.getCell();*/
            return new BoardCell();
        });
        // Make sure it's unselected, so when you return to this view
        // it looks the same as before.
        this.boards.getSelectionModel().clearSelection();
        
        // When you select (i.e.) click a board, open that board.
        this.boards.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        long boardId = newValue.getId();
                        mainCtrl.showOverview(boardId);
                        sockets.listenForBoard(boardId);
                    }
                });
    }

    /**
     * Disconnect from server. When called, the Stompsession is ended
     * and scene is set up back to ConnectServerCtrl
     *
     * @param mouseEvent the mouse event - unused
     */
    public void disconnect(MouseEvent mouseEvent) {
        sockets.getSession().disconnect();
        System.out.println("The client has been disconnected");

        mainCtrl.showConnect();
    }

    /**
     * Add a new board
     *
     * @param mouseEvent the mouse event
     */
    public void addBoard(MouseEvent mouseEvent) {
        mainCtrl.showAddBoard();
    }

    /**
     * Go to the interface to create a new board
     *
     * @param mouseEvent the mouse event
     */
    public void newBoard(MouseEvent mouseEvent) {
        mainCtrl.showCreateBoard();
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
     * Setter for admin
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.isAdmin = isAdmin;
    }
}
