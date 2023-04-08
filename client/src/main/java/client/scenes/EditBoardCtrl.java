package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class EditBoardCtrl {

    private final MainCtrl mainCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final ServerUtils serverUtils;
    private final ClientConfig config;

    private Board board;

    @FXML
    private Parent root;
    @FXML
    private TextField boardTitle;
    @FXML
    private TextField boardId;


    /**
     * Constructor
     * @param mainCtrl the main controller
     * @param boardOverviewCtrl the board overview controller
     * @param serverUtils the server utils - used to send changes to server
     * @param config the client config - stores the boards you joined
     */
    @Inject
    public EditBoardCtrl(MainCtrl mainCtrl,
                         BoardOverviewCtrl boardOverviewCtrl,
                         ServerUtils serverUtils,
                         ClientConfig config) {
        this.mainCtrl = mainCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.serverUtils = serverUtils;
        this.config = config;
    }

    /**
     * Setter for the board
     * @param board the board to edit
     */
    public void setBoard(Board board) {
        this.board = board;
    }
    /**
     * Show the popup to edit board details for the given board.
     * It uses the board with which {@link this.setBoard() } is called.
     */
    public void refresh() {
        this.boardTitle.setText(board.getTitle());
        this.boardId.setText(String.valueOf(board.getId()));
    }

    /**
     * This deletes the board, and return the client to the list of boards
     */
    @FXML
    public void delete() {
        long boardId = board.getId();
        serverUtils.deleteBoard(boardId);
        config.removeBoard(serverUtils.getHostname(), boardId);
        mainCtrl.saveConfig("Next time you open talio, "
                + "you might get an error about others having deleted your board");
        boardOverviewCtrl.hidePopup();
        boardOverviewCtrl.returnToBoardList();
    }

    /**
     * This cancels making changes to the board
     * and closes the popup.
     */
    @FXML
    public void close() {
        boardOverviewCtrl.hidePopup();
    }

    /**
     * This changes the title by sending the change to the server,
     * so that then every client will receive the change.
     */
    @FXML
    public void save() {
        // We create a new board without cardlist to send to server
        long id = this.board.getId();
        String title = this.boardTitle.getText();
        Board updatedBoard = new Board(id, title);
        serverUtils.updateBoard(updatedBoard);

        // The UI will be updated when we get the info back via long-polling/websockets

        boardOverviewCtrl.hidePopup();
    }

}
