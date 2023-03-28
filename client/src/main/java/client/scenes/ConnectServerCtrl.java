package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectServerCtrl implements Initializable {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private TextField hostnameField;

    /**
     * Creates a ConnectServerCtrl with the given server utils and main controller.
     * This class relies on injection so the constructor should not be called manually.
     * @param server
     * @param mainCtrl
     */
    @Inject
    public ConnectServerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Currently does nothing.
     * TODO: see whether this has any future use and if not remove it.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostnameField.setText("localhost");
    }

    /**
     * This method is called when the user clicks on the connect button.
     * It should connect to the server (currently not implemented),
     * and it then shows the board overview.
     */
    public void connect() {
        server.setHostnameAndConnect(hostnameField.getText());
        mainCtrl.showListOfBoards();
    }

}
