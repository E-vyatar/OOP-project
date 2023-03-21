package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ConnectServerCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField addressServer;

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

    }

    /**
     * This method is called when the user clicks on the connect button.
     * It should connect to the server (currently not implemented),
     * and it then shows the board overview.
     * @throws UnknownHostException currently not thrown
     * TODO: have it actually set the server address.
     * In addition it should probably not thrown an exception since this is called by the UI.
     * you probably want to have an Error dialog shown.
     */
    public void connect() throws UnknownHostException {
//        server.setAddress(InetAddress.getByName(addressServer.getText()));
        mainCtrl.showOverview();
    }

}
