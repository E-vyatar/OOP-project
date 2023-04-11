package client.scenes;

import client.ClientConfig;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

public class CreateBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final ClientConfig config;

    @FXML
    private TextField boardTitle;

    /**
     * Create an instance of CreateBoardCtrl. This controller manages the view,
     * to create a new board.
     * @param mainCtrl the main controller. Used to switch to other scenes.
     * @param server the server utils. Used to tell to server to create a new board.
     * @param config the client configuration. Used to add the created board to.
     */
    @Inject
    public CreateBoardCtrl(MainCtrl mainCtrl, ServerUtils server, ClientConfig config) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
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
            board = server.addBoard(board);
            // Remember board
            config.addBoard(server.getHostname(), board.getId());
            mainCtrl.saveConfig("The created board might not show up next time you run the talio.");
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
