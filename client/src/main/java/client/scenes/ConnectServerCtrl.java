package client.scenes;

import client.utils.PollingUtils;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectServerCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final PollingUtils polling;
    private final SocketsUtils sockets;

    @FXML
    private TextField hostnameField;

    /**
     * Creates a ConnectServerCtrl with the given server utils and main controller.
     * This class relies on injection so the constructor should not be called manually.
     *
     * @param server the ServerUtils of the app
     * @param polling  the PollingUtils of the app
     * @param sockets utils class for sockets so we can start connection
     * @param mainCtrl the MainCtrl of the app
     */
    @Inject
    public ConnectServerCtrl(ServerUtils server,
                             PollingUtils polling,
                             SocketsUtils sockets,
                             MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.polling = polling;
        this.sockets = sockets;
    }

    /**
     * Currently it only autofills server input with "localhost)
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostnameField.setText("localhost");
    }

    /**
     * Connects to server
     */
    public void connect() {
        String hostname = hostnameField.getText();
        server.setHostnameAndConnect(hostname);
        polling.setHostname(hostname);
        sockets.setHostnameAndConnect(hostname);
        mainCtrl.showListOfBoards();
    }

}
