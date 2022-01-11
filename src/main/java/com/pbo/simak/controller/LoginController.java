package com.pbo.simak.controller;

import com.pbo.simak.middleware.Authentication;
import com.pbo.simak.model.UserModel;
import com.pbo.simak.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LoginController {
    private final HashMap<String, String> storeData = new HashMap<>();


    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginMessage;


    @FXML
    private void registerPage(MouseEvent mouseEvent) throws IOException {
        SceneUtils.switchTo("register.fxml", mouseEvent);
    }

    @FXML
    private void loginAction(ActionEvent actionEvent) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        storeData.put("email", email.getText());
        storeData.put("password", password.getText());
        if (!email.getText().isBlank() && !password.getText().isBlank()) {
            ResultSet rs = UserModel.getByEmail(email.getText());

            if (rs.next() == false) {
                loginMessage.setText("User Not Found");
            } else {
                do {
                    String passwordHashed = rs.getString("password");
                    if (Authentication.validatePassword(password.getText(), passwordHashed)) {
                        SceneUtils.switchTo("dashboard.fxml", actionEvent);
                    } else {
                        loginMessage.setText("User or Password Incorrect");
                    }
                } while (rs.next());
            }
        } else {
            loginMessage.setText("Please Input Email and Password");
        }
    }
}
