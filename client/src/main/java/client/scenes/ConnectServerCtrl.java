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

    @Inject
    public ConnectServerCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = mainCtrl.getServer();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostnameField.setText("localhost");
    }

    public void connect() {
        server.setHostnameAndConnect(hostnameField.getText());
        mainCtrl.showOverview(0);
    }

}
