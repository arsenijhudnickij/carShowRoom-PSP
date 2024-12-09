package main.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import main.models.entities.Session;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.models.entities.Admin;
import main.models.entities.Person;
import main.models.entities.Role;
import main.models.entities.User;
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
    private void initialize()
    {
        regLink.setOnMouseEntered(event -> regLink.setStyle("-fx-underline: true; -fx-cursor: hand;"));
        regLink.setOnMouseExited(event -> regLink.setStyle("-fx-underline: true; -fx-cursor: default;"));
        regLink.setOnMouseClicked(event -> openRegistrationWindow());

        signInButton.setOnAction(event -> handleSignIn());
    }
    //sign in
    private void handleSignIn()
    {
        clearErrorLabels();

        String login = loginField.getText().trim();
        String password = passwordField.getText();

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

        if (valid) {
            String hashedPassword = hashPassword(password);
            Person person = new Person(login, hashedPassword);

            Object userOrAdmin = loginPerson(person);
            if (userOrAdmin != null) {
                Integer roleId = null;

                if (userOrAdmin instanceof User) {
                    User user = (User) userOrAdmin;
                    roleId = user.getPerson().getRole().getRoleId();
                    System.out.println("Logged in as User, role_id: " + roleId);
                    Session.setUser(user);
                } else if (userOrAdmin instanceof Admin) {
                    Admin admin = (Admin) userOrAdmin;
                    roleId = admin.getPerson().getRole().getRoleId();
                    System.out.println("Logged in as Admin, role_id: " + roleId);
                    Session.setAdmin(admin);
                }

                if (roleId != null) {
                    clearFields();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Успешная авторизация");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Вы успешно авторизованы!");
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

    //server
    private Object loginPerson(Person person)
    {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(person));
        request.setRequestType(RequestType.LOGIN);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);

                Response response = new Gson().fromJson(responseJson, Response.class);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    JsonObject responseJsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
                    JsonObject dataObject = responseJsonObject.getAsJsonObject("data");

                    if (dataObject != null) {
                        if (dataObject.has("userId")) {
                            return new Gson().fromJson(dataObject, User.class);
                        } else if (dataObject.has("id")) {
                            return new Gson().fromJson(dataObject, Admin.class);
                        }
                    }
                } else {
                    System.out.println("Error: " + response.getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //open windows
    private void openWindow(String fxmlFile, String windowTitle)
    {
        try {
            SceneSwitcher.switchScene(fxmlFile, signInButton, 1385, 740, windowTitle);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить файл " + fxmlFile);
        }
    }
    private void openRegistrationWindow()
    {
        try {
            SceneSwitcher.switchSceneStart("registration.fxml", regLink, "Registration");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить файл registration.fxml");
        }
    }

    //clear
    private void clearErrorLabels()
    {
        loginLabel.setText("");
        passwordLabel.setText("");
        repPassLabel.setText("");
    }
    private void clearFields()
    {
        loginField.clear();
        passwordField.clear();
    }

    //hash
    private String hashPassword(String password)
    {
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
}
