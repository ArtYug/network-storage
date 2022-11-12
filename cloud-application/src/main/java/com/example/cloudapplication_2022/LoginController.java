package com.example.cloudapplication_2022;

import com.geekbrains.cloud2022.AuthRequest;
import com.geekbrains.cloud2022.AuthSuccess;
import com.geekbrains.cloud2022.CloudMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
public class LoginController implements Initializable {

    public TextField loginField;
    public PasswordField passwordField;
    public Button enterLoginButton;
    public HBox hbox_for_login;
    private static Network network;
    public Text textAreaLoginExist;
    public CheckBox checkboxShowPassword;
    public TextField passwordText;
    public TextField text;
    public Text textCheck;

    public static Network getNetwork() {
        return network;
    }
    private void readLoop() {
        try {
            while (true) {
                CloudMessage message = network.read();
                if (message instanceof AuthSuccess response) {
                    if (!response.isAuth()) {
                        System.out.println("You are not registered. Please, log in");
                        textCheck.setText("You are not registered. Please, try again or Sign up");
                    } else {
                        break;
                    }
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            CloudApplication.scene.setRoot(root);
           // stage.setTitle("cloud application");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            enterLoginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        authenticate();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            network = new Network(8189);
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
            passwordField.setVisible(true);
            passwordText.setVisible(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticate() throws IOException {
        String log = loginField.getText();
        String password = passwordField.getText();
        network.write(new AuthRequest(log, password));
    }

    @FXML
    public void changeVisibilityTest() {
        SignUpController.showPasswordCheckBox(checkboxShowPassword, passwordText, passwordField);
    }

    public void signUpMethod(ActionEvent event) throws IOException {
        Stage stage = (Stage) hbox_for_login.getScene().getWindow();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("signup.fxml"))));
        stage.setTitle("Sign up");
        stage.setScene(new Scene(root));
    }
}

