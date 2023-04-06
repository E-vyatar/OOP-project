package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class JoinBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ClientConfig clientConfig;
    private final ServerUtils server;

    @FXML
    private TextField boardId;

    /**
     * Constructor of JoinBoardCtrl. This is the controller for joining a board.
     * @param mainCtrl the main controller
     * @param clientConfig the configuration of the client - stores the boards
     * @param server - the server utils
     */
    @Inject
    public JoinBoardCtrl(MainCtrl mainCtrl, ClientConfig clientConfig, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.clientConfig = clientConfig;
        this.server = server;
    }

    /**
     * Initalize the controller.
     * It adds a listener so that a red border appears when you enter an invalid board ID
     */
    public void initialize() {
        this.boardId.textProperty().addListener((observable, oldValue, newValue) -> {
            var classes = boardId.getStyleClass();
            if (!this.isValid(newValue)) {
                if (!classes.contains("invalid")) {
                    classes.add("invalid");
                }
            } else {
                classes.remove("invalid");
            }
        });
    }

    /**
     * Cancel joining a board and go back to the list of boards.
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
     * Try to join the board. Called when the join button is pressed.
     */
    @FXML
    private void join() {
        if (!isValid(boardId.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid board ID");
            alert.setContentText("Invalid board ID. " +
                    "Only positive integers are valid IDs. Please check for typos");
            alert.show();
        } else {
            long id = Long.parseLong(boardId.getText());
            joinBoard(id);
        }
    }
    /**
     * Try to join the given board.
     * Only joins the board if you don't already have the board and/or it doesn't exist
     *
     * @param boardId the id of the board
     */
    public void joinBoard(long boardId) {
        if (clientConfig.hasBoard(server.getHostname(), boardId)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("You have already joined this board.");
            alert.setContentText("You can't join an already joined board. " +
                    "If you meant to join a new board, " +
                    "please check you entered the correct");
            alert.show();
        } else {
            boolean exists = server.boardExists(boardId);
            if (exists) {
                clientConfig.addBoard(server.getHostname(), boardId);
                mainCtrl.saveConfig("The joined board might not show up next time.");
                mainCtrl.showListOfBoards();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Board not found");
                alert.setContentText("Can't find the board for id " + boardId +
                        ". Verify you have entered the correct board id.");
                alert.show();
            }
        }
    }


    /**
     * Check whether the board ID has a valid format (i.e. is a positive number
     * @param boardId the boardId for which to check if it's a valid format
     * @return whether it's valid
     */
    public boolean isValid(String boardId) {
        try {
            long id = Long.parseLong(boardId);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
