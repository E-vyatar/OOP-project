package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;

public class EditBoardCtrl {

    private final ServerUtils serverUtils;

    private Stage popup;

    private Board board;

    @FXML
    private Parent root;
    @FXML
    private TextField boardTitle;
    @FXML
    private TextField boardId;


    /**
     * Constructor
     * @param serverUtils the server utils - used to send changes to server
     */
    @Inject
    public EditBoardCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Initialize the controller.
     * This will create a stage for the scene.
     */
    public void initialize() {
        this.popup = new Stage();
        this.popup.initModality(Modality.APPLICATION_MODAL);
        this.popup.setMinWidth(240.0);
        this.popup.setMinHeight(200.0);
        this.popup.setScene(new Scene(root));
    }

    /**
     * Show the popup to edit board details for the given board
     * @param board the board to edit
     */
    public void show(Board board) {
        this.board = board;
        this.boardTitle.setText(board.getTitle());
        this.boardId.setText(String.valueOf(board.getId()));
        this.popup.show();
    }

    /**
     * This deletes the board
     */
    @FXML
    private void delete() {
        // TODO: write code
    }

    /**
     * This cancels making changes to the board
     * and closes the popup.
     */
    @FXML
    private void close() {
        popup.hide();
    }

    /**
     * This changes the title by sending the change to the server,
     * so that then every client will receive the change.
     */
    @FXML
    private void save() {

    }

}
