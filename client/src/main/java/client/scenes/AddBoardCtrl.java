package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ClientConfig clientConfig;
    private final ServerUtils server;

    @FXML
    private TextField boardId;

    /**
     * Constructor of AddBoardCtrl. This is the controller for adding a board to your list.
     * @param mainCtrl the main controller
     * @param clientConfig the configuration of the client - stores the boards
     * @param server - the server utils
     */
    @Inject
    public AddBoardCtrl(MainCtrl mainCtrl, ClientConfig clientConfig, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.clientConfig = clientConfig;
        this.server = server;
    }

    /**
     * Cancel adding a board and go back to the list of boards.
     */
    public void cancel() {
        mainCtrl.showListOfBoards();
    }

    /**
     * Clear the input field.
     */
    public void clear() {
        boardId.setText("");
    }

    /**
     * Add the board.
     */
    public void add() {
        long id = Long.parseLong(boardId.getText());;
        clientConfig.addBoard(server.getHostname(), id);
        mainCtrl.saveConfig("The joined board might not show up next time.");
        mainCtrl.showListOfBoards();
    }
}
