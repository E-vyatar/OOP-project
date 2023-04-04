package client.scenes;

import client.utils.PollingUtils;
import client.utils.ServerUtils;
import client.utils.SocketsUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectServerCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final PollingUtils polling;
    private final SocketsUtils sockets;

    @FXML
    private TextField hostnameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TitledPane advancedOptions;

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
     * This method is called when the user clicks on the connect button.
     * It should connect to the server,
     * and it then shows the list of boards.
     *
     * @param hostname the hostname of the server
     */
    public void connect(String hostname) {
        server.setHostname(hostname);
        polling.setHostname(hostname);
        sockets.setHostnameAndConnect(hostname);
        mainCtrl.showListOfBoards();
    }
    /**
     * This method is called when the user connects as the admin.
     *
     * @param hostname the server to connect to
     * @param password the password of the admin
     */
    public void connect(String hostname, String password) {
        server.setHostnameAndPassword(hostname, password);
        polling.setHostname(hostname);
        sockets.setHostnameAndConnect(hostname);
        mainCtrl.showListOfBoards();
    }

    /**
     * Try connecting to the server,
     * checks for authentication if applicable.
     */
    public void tryConnect() {
        // Clear styles
        hostnameField.setStyle("");
        passwordField.setStyle("");

        String hostname = hostnameField.getText();
        String password = passwordField.getText();

        if (password.isEmpty()) {
            connect(hostname);
        }else {
            try {
                System.out.println("password: " + password);
                if (server.isAuthenticated(hostname, password)) {
                    connect(hostname, password);
                } else {
                    System.out.println("password incorrect");
                    // We've been given the incorrect password
                    advancedOptions.setExpanded(true);
                    // We want to highlight the password
                    passwordField.setStyle("-fx-border-color: red ;");
                }

            } catch (ProcessingException e) {
                // We couldn't connect to the server
                hostnameField.setStyle("-fx-border-color: red;");
            }
        }

    }
}
