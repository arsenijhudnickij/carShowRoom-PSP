package main.controllers;

import com.google.gson.Gson;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javafx.util.Duration;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.enums.RoleName;
import main.models.entities.Person;
import main.models.entities.Role;
import main.models.entities.Session;
import main.models.entities.User;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

public class RegistrationController {
    @FXML
    private TextField FIOField;
    @FXML
    private TextField passportNumField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField seсondPassField;
    @FXML
    private Button signUpButton;
    @FXML
    private Label authLink;
    @FXML
    private Label FIOLabel;
    @FXML
    private Label passportLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label repPassLabel;

    @FXML
    private void initialize() {
        authLink.setOnMouseClicked(event -> openAuthorizationWindow());
        authLink.setOnMouseEntered(event -> authLink.setStyle("-fx-underline: true; -fx-cursor: hand;"));
        authLink.setOnMouseExited(event -> authLink.setStyle("-fx-underline: true; -fx-cursor: default;"));
        signUpButton.setOnAction(event -> validateUser());
    }

    private void validateUser() {
        clearErrorLabels();

        String fio = FIOField.getText().trim();
        String passportNum = passportNumField.getText().trim();
        LocalDate localDateOfBirth = dateField.getValue();
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = seсondPassField.getText();

        boolean valid = true;

        if (fio.isEmpty()) {
            FIOLabel.setText("Не должно быть пустым");
            FIOLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateFIO(fio)) {
            FIOLabel.setText("Некорректный формат ФИО");
            FIOLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (passportNum.isEmpty()) {
            passportLabel.setText("Не должно быть пустым");
            passportLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validatePassportNum(passportNum)) {
            passportLabel.setText("Некорректный номер паспорта");
            passportLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (localDateOfBirth == null) {
            dateLabel.setText("Введите дату рождения");
            dateLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else {
            LocalDate now = LocalDate.now();
            if (localDateOfBirth.plusYears(18).isAfter(now)) {
                dateLabel.setText("Возраст должен быть больше 18 лет");
                dateLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                valid = false;
            }
        }

        if (login.isEmpty()) {
            loginLabel.setText("Не должно быть пустым");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateSymbolNumber(login)) {
            loginLabel.setText("От 8 до 16 символов");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateNoRussianLetters(login)) {
            loginLabel.setText("Без кириллицы");
            loginLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (password.isEmpty()) {
            passwordLabel.setText("Не должно быть пустым");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateSymbolNumber(password)) {
            passwordLabel.setText("От 8 до 16 символов");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateNoRussianLetters(password)) {
            passwordLabel.setText("Без кириллицы");
            passwordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (confirmPassword.isEmpty()) {
            repPassLabel.setText("Не должно быть пустым");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }
        else if (!validateSymbolNumber(password)) {
            repPassLabel.setText("От 8 до 16 символов");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        } else if (!validateNoRussianLetters(password)) {
            repPassLabel.setText("Без кириллицы");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }
        else if (!password.equals(confirmPassword)) {
            repPassLabel.setText("Пароли не совпадают");
            repPassLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            valid = false;
        }

        if (valid) {
            int birthDate = 0;
            int birthMonth = 0;
            int birthYear = 0;
            if (localDateOfBirth != null) {
                birthDate = localDateOfBirth.getDayOfMonth();
                birthMonth = localDateOfBirth.getMonthValue();
                birthYear = localDateOfBirth.getYear();
            }
            Role role = new Role(1, RoleName.USER);
            String hashedPassword = hashPassword(password);
            Person person = new Person(login, hashedPassword, role);
            int checker = checkPersonExists(person);
            if ( checker >= 0) {
                person.setPersonId(checker);
                User user = new User(fio, passportNum, birthDate, birthMonth, birthYear, person);
                User registeredUser = sendUserToServer(user);
                clearFields();

                if (registeredUser != null) {
                    Session.setUser(registeredUser);

                    String roleName = user.getPerson().getRole().getRoleName().name();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Успешная регистрация");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Пользователь успешно зарегистрирован с ролью: " + roleName);
                    successAlert.showAndWait();
                    try {
                        SceneSwitcher.switchScene("user.fxml", signUpButton, 1385, 740,"DEALS DRIVE");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Не удалось загрузить файл registration.fxml");
                    }
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Ошибка регистрации");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Ошибка при регистрации пользователя. Попробуйте снова.");
                    errorAlert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка регистрации");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь с таким логином уже существует");
                alert.showAndWait();

                loginField.clear();
            }
        }
    }

    public static boolean validateSymbolNumber(String str) {
        return str.length() >= 8 && str.length() <= 16;
    }
    public static boolean validateNoRussianLetters(String str) {
        return !str.matches(".*[а-яА-Я].*");
    }
    private void clearFields() {
        FIOField.clear();
        passportNumField.clear();
        dateField.setValue(null);
        loginField.clear();
        passwordField.clear();
        seсondPassField.clear();
    }
    private void clearErrorLabels() {
        FIOLabel.setText("");
        passportLabel.setText("");
        dateLabel.setText("");
        loginLabel.setText("");
        passwordLabel.setText("");
        repPassLabel.setText("");
    }
    private boolean validateFIO(String fio) {
        String[] parts = fio.split(" ");
        if (parts.length != 3) {
            return false;
        }
        for (String part : parts) {
            if (!part.matches("([A-ZА-Я][a-zа-яёЁ]+)")) {
                return false;
            }
        }
        return true;
    }
    private boolean validatePassportNum(String passportNum) {
        return passportNum.matches("[A-Z]{2}\\d{6}");
    }
    private void openAuthorizationWindow() {
        try {
            SceneSwitcher.switchSceneStart("authorization.fxml", authLink, "Authorization");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить authorization.fxml");
        }
        System.out.println("switch to authorization");
    }
    private int checkPersonExists(Person person) {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(person));
        request.setRequestType(
                RequestType.CHECK_PERSON);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);

                Response response = new Gson().fromJson(responseJson, Response.class);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    try {
                        String dataString = response.getData().toString();
                        if (dataString.endsWith(".0")) {
                            dataString = dataString.substring(0, dataString.length() - 2);
                        }
                        try {
                            return Integer.parseInt(dataString);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return -1;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
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
    private User sendUserToServer(User user) {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(user));
        request.setRequestType(RequestType.SIGNUP);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String serverResponse;
        try {
            serverResponse = ClientSocket.getInstance().getIn().readLine();
            if (serverResponse != null) {
                System.out.println("Response from server: " + serverResponse);
                Response response = new Gson().fromJson(serverResponse, Response.class);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    return new Gson().fromJson(new Gson().toJson(response.getData()), User.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}