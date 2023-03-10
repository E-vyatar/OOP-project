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

    @Inject
    public ConnectServerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void connect() throws UnknownHostException {
//        server.setAddress(InetAddress.getByName(addressServer.getText()));
        mainCtrl.showOverview();
    }

}
