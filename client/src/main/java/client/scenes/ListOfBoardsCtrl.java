package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;

/**
 * The controller for the list of boards.
 * The list of boards is the interface that allows you
 * to select which board you want to view. It also has
 * buttons to disconnect from the server, add a pre-existing
 * board and to create a new board.
 */
public class ListOfBoardsCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    @FXML
    private ListView<Board> boards;

    /**
     * This constructs an instance of ListOfBoards.
     *
     * @param mainCtrl    the main controller
     * @param serverUtils the server utils
     */
    @Inject
    public ListOfBoardsCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * Refresh the controller.
     * This loads data from the backend and sets the listView.
     */
    public void refresh() {
        ObservableList<Board> data = FXCollections.observableList(serverUtils.getBoards());
        this.boards.setItems(data);
        this.boards.setCellFactory(new Callback<ListView<Board>, ListCell<Board>>() {
            @Override
            public ListCell<Board> call(ListView<Board> param) {
                /*BoardCellCtrl boardCellCtrl = new BoardCellCtrl();
                return boardCellCtrl.getCell();*/
                return new BoardCell();
            }
        });
        // When you select (i.e.) click a board, open that board.
        this.boards.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Board>() {
            @Override
            public void changed(ObservableValue<? extends Board> observable, Board oldValue, Board newValue) {
                if (newValue != null) {
                    mainCtrl.showOverview(0);
                }
            }
        });
    }

    /**
     * Disconnect from server
     *
     * @param mouseEvent the mouse event
     */
    public void disconnect(MouseEvent mouseEvent) {
        throw new NotImplementedException();
    }

    /**
     * Add a new board
     *
     * @param mouseEvent the mouse event
     */
    public void addBoard(MouseEvent mouseEvent) {
        throw new NotImplementedException();
    }

    /**
     * Create a new board
     *
     * @param mouseEvent the mouse event
     */
    public void newBoard(MouseEvent mouseEvent) {
        throw new NotImplementedException();
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
}
