package NetChat.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {
    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    Button btn1;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

    @FXML
    TextField loginfield;

    @FXML
    PasswordField passwordfield;

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    private boolean isAuthorised;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public void setAuthorised(boolean isAuthorised) {
        this.isAuthorised = isAuthorised;
        if (!isAuthorised) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        }
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorised(true);
                                break;
                            } else {
                                textArea.appendText(str + "\n");
                            }
                        }
                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/serverClosed")) {
                                setAuthorised(false);
                                break;
                            }
                            textArea.appendText(str + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth() {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/auth " + loginfield.getText() + " " + passwordfield.getText());
            loginfield.clear();
            passwordfield.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Dispose() {
        try {
            out.writeUTF("/end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}