package main.controllers;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.models.entities.Person;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

public class AuthorizationController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passRepField;
    @FXML
    private Button signInButton;
    @FXML
    private Label regLink;
    @FXML
    private Label loginLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label repPassLabel;

    @FXML
    private void initialize() {
        regLink.setOnMouseEntered(event -> regLink.setStyle("-fx-underline: true; -fx-cursor: hand;"));
        regLink.setOnMouseExited(event -> regLink.setStyle("-fx-underline: true; -fx-cursor: default;"));
        regLink.setOnMouseClicked(event -> openRegistrationWindow());

        signInButton.setOnAction(event -> handleSignIn());
    }

    private void handleSignIn() {
        clearErrorLabels();

        String login = loginField.getText().trim();
        String password = passwordField.getText();
        String repPassword = passRepField.getText();

        boolean valid = true;

        if (login.isEmpty()) {
            loginLabel.setText("Не должно быть пустым");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!RegistrationController.validateSymbolNumber(login)) {
            loginLabel.setText("От 8 до 16 символов");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!RegistrationController.validateNoRussianLetters(login)) {
            loginLabel.setText("Без кириллицы");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (password.isEmpty()) {
            passwordLabel.setText("Не должно быть пустым");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!RegistrationController.validateSymbolNumber(password)) {
            passwordLabel.setText("От 8 до 16 символов");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!RegistrationController.validateNoRussianLetters(password)) {
            passwordLabel.setText("Без кириллицы");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (repPassword.isEmpty()) {
            repPassLabel.setText("Не должно быть пустым");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!password.equals(repPassword)) {
            repPassLabel.setText("Пароли не совпадают");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!RegistrationController.validateNoRussianLetters(repPassword)) {
            repPassLabel.setText("Без кириллицы");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (valid) {
            String hashedPassword = hashPassword(password);
            Person person = new Person(login, hashedPassword);

            Response response = loginPerson(person);
            if (response != null && response.getResponseStatus() == ResponseStatus.OK) {
                Number roleIdNumber = (Number) response.getData();
                Integer roleId = roleIdNumber.intValue();
                System.out.println("Successful login, role_id: " + roleId);
                clearFields();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешная авторизация");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Пользователь успешно авторизован!");
                successAlert.showAndWait();
                switch (roleId) {
                    case 1:
                        openWindow("user.fxml", "DEALS DRIVE");
                        break;
                    case 2:
                        openWindow("worker.fxml", "DEALS DRIVE");
                        break;
                    case 3:
                        openWindow("admin.fxml", "DEALS DRIVE");
                        break;
                    default:
                        System.out.println("Неизвестная роль: " + roleId);
                        break;
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка авторизации");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Неверный логин или пароль. Попробуйте снова.");
                errorAlert.showAndWait();
                clearFields();
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при хешировании пароля", e);
        }
    }

    private void clearErrorLabels() {
        loginLabel.setText("");
        passwordLabel.setText("");
        repPassLabel.setText("");
    }

    private void clearFields() {
        loginField.clear();
        passwordField.clear();
        passRepField.clear();
    }

    private Response loginPerson(Person person) {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(person));
        request.setRequestType(RequestType.LOGIN);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                return new Gson().fromJson(responseJson, Response.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openWindow(String fxmlFile, String windowTitle) {
        try {
            SceneSwitcher.switchScene(fxmlFile, signInButton, 1385, 740, windowTitle);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить файл " + fxmlFile);
        }
    }
    private void openRegistrationWindow() {
        try {
            SceneSwitcher.switchScene("registration.fxml", regLink, 650, 400,"Registration");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить файл registration.fxml");
        }
    }
}
