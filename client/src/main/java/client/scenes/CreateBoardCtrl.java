package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

public class CreateBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private TextField boardTitle;

    /**
     * Create an instance of CreateBoardCtrl. This controller manages the view,
     * to create a new board.
     * @param mainCtrl the main controller. Used to switch to other scenes.
     * @param server the server utils. Used to tell to server to create a new board.
     */
    @Inject
    public CreateBoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Cancel creating a new board and go back to the list of boards
     * @param mouseEvent unused
     */
    public void cancel(MouseEvent mouseEvent) {
        mainCtrl.showListOfBoards();
    }

    /**
     * Create a bord with the given title and go back to the list of boards
     * @param mouseEvent
     */
    public void create(MouseEvent mouseEvent) {
        if (this.boardTitle.getText().isEmpty()) {
            // TODO: give a warning
        } else {
            String title = this.boardTitle.getText();
            Board board = new Board();
            board.setTitle(title);
            server.addBoard(board);
            mainCtrl.showListOfBoards();
        }
    }

    /**
     * Clears the textfield to specify the title and set it to "untitled".
     * Without this method, it would keep the title of the previous added board.
     */
    public void clear() {
        this.boardTitle.setText("Untitled");
    }
}
