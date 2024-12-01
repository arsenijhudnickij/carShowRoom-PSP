package main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.models.entities.Admin;
import main.models.entities.Session;
import main.models.entities.User;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminPageController {
    @FXML
    private HBox addCar;
    @FXML
    private HBox giveRole;
    @FXML
    private HBox deleteWorker;
    @FXML
    private ComboBox<String> checkCar;
    @FXML
    private ComboBox<String> profile;
    @FXML
    private Button aboutUs;
    @FXML
    private Label roleField;
    @FXML
    private Label loginField;
    @FXML
    private AnchorPane startPanel;
    @FXML
    private AnchorPane addCarPanel;
    @FXML
    private AnchorPane giveRolePanel;
    @FXML
    private AnchorPane deleteWorkerPanel;
    @FXML
    private AnchorPane aboutUsPanel;
    @FXML
    private AnchorPane myprofilePanel;
    @FXML
    private AnchorPane viewCarsPanel;
    @FXML
    private AnchorPane profilePanel;
    @FXML
    private AnchorPane developerPane;
    @FXML
    private AnchorPane aboutCompanyPane;
    @FXML
    private AnchorPane connectionPane;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> passportColumn;
    @FXML
    private TableColumn<User, String> birthDateColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> patronymicColumn;
    @FXML
    private TableColumn<User, CheckBox> checkboxColumn;
    @FXML
    private Button sendNewRoles;
    @FXML
    private TableView<User> workerTable;
    @FXML
    private TableColumn<User, String> nameColumnWorker;
    @FXML
    private TableColumn<User, String> passportColumnWorker;
    @FXML
    private TableColumn<User, String> birthDateColumnWorker;
    @FXML
    private TableColumn<User, String> surnameColumnWorker;
    @FXML
    private TableColumn<User, String> patronymicColumnWorker;
    @FXML
    private TableColumn<User, CheckBox> checkboxColumnWorker;
    @FXML
    private Button deleteWorkers;

    private Map<User, Boolean> userSelectionMap = new HashMap<>();
    private Map<User, Boolean> workerSelectionMap = new HashMap<>();

    @FXML
    public void initialize() {
        surnameColumn.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String surname = nameParts.length > 0 ? nameParts[0] : "";
            return new SimpleStringProperty(surname);
        });

        nameColumn.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String name = nameParts.length > 1 ? nameParts[1] : "";
            return new SimpleStringProperty(name);
        });

        patronymicColumn.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String patronymic = nameParts.length > 2 ? nameParts[2] : "";
            return new SimpleStringProperty(patronymic);
        });

        passportColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPassportNum()));

        birthDateColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            String birthDate = user.getBirthDate() + "." + user.getBirthMonth() + "." + user.getBirthYear();
            return new SimpleStringProperty(birthDate);
        });

        checkboxColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(userSelectionMap.getOrDefault(user, false));

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                userSelectionMap.put(user, newValue);
            });

            return new SimpleObjectProperty<>(checkBox);
        });

        initHandlers();
        initProfileHandler();

        surnameColumnWorker.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String surname = nameParts.length > 0 ? nameParts[0] : "";
            return new SimpleStringProperty(surname);
        });

        nameColumnWorker.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String name = nameParts.length > 1 ? nameParts[1] : "";
            return new SimpleStringProperty(name);
        });

        patronymicColumnWorker.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String patronymic = nameParts.length > 2 ? nameParts[2] : "";
            return new SimpleStringProperty(patronymic);
        });

        passportColumnWorker.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPassportNum()));

        birthDateColumnWorker.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            String birthDate = user.getBirthDate() + "." + user.getBirthMonth() + "." + user.getBirthYear();
            return new SimpleStringProperty(birthDate);
        });

        checkboxColumnWorker.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(workerSelectionMap.getOrDefault(user, false));

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                workerSelectionMap.put(user, newValue);
            });

            return new SimpleObjectProperty<>(checkBox);
        });
    }

    private void initHandlers() {
        addCar.setOnMouseClicked(event -> handleAddCar());
        giveRole.setOnMouseClicked(event -> handleGiveRole());
        deleteWorker.setOnMouseClicked(event -> handleDeleteWorker());
        aboutUs.setOnMouseClicked(event -> handleAboutUs());
        sendNewRoles.setOnMouseClicked(event -> saveRolesOnServer( userSelectionMap));
        deleteWorkers.setOnMouseClicked(event -> deleteWorkersOnServer( workerSelectionMap));
    }

    private void initProfileHandler() {
        profile.setOnAction(event -> {
            String selectedItem = profile.getValue();
            if ("Выход".equals(selectedItem)) {
                showLogoutConfirmation();
            } else if ("Мой профиль".equals(selectedItem)) {
                showProfilePanel();
            } else if ("О компании".equals(selectedItem)) {
                showAboutCompanyPanel();
            }
        });
    }

    private void showProfilePanel() {
        hideAllPanels();
        profilePanel.setVisible(true);
        myprofilePanel.setVisible(true);

        Admin admin = Session.getAdmin();
        if (admin != null) {
            roleField.setText(admin.getPerson().getRole().getRoleName().toString());
            loginField.setText(admin.getPerson().getLogin());
        } else {
            System.out.println("Нет информации о администраторе в сессии.");
        }
    }

    private void showAboutCompanyPanel() {
        hideAllPanels();
        aboutUsPanel.setVisible(true);
        developerPane.setVisible(true);
        aboutCompanyPane.setVisible(true);
        connectionPane.setVisible(true);
    }

    private void hideAllPanels() {
        addCarPanel.setVisible(false);
        startPanel.setVisible(false);
        giveRolePanel.setVisible(false);
        deleteWorkerPanel.setVisible(false);
        aboutUsPanel.setVisible(false);
        viewCarsPanel.setVisible(false);
        profilePanel.setVisible(false);
        myprofilePanel.setVisible(false);
        developerPane.setVisible(false);
        aboutCompanyPane.setVisible(false);
        connectionPane.setVisible(false);
    }

    private void showLogoutConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение выхода");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        alert.setContentText("Нажмите ОК для выхода или Отмена для возврата.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Session.clearSession();
                System.out.println("Admin logged out.");
                openAuthorizationWindow();
            } else {
                profile.setValue("Мой профиль");
            }
        });
    }

    private void handleAddCar() {
        System.out.println("Adding a car...");
    }

    private void handleGiveRole() {
        hideAllPanels();
        giveRolePanel.setVisible(true);
        System.out.println("Requesting all users...");

        List<User> users = getUsersFromServer();
        if (!users.isEmpty()) {
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getPerson() != null
                            && user.getPerson().getRole() != null
                            && "USER".equalsIgnoreCase(user.getPerson().getRole().getRoleName().toString()))
                    .collect(Collectors.toList());

            ObservableList<User> userObservableList = FXCollections.observableArrayList(filteredUsers);
            userTable.setItems(userObservableList);

            for (User user : filteredUsers) {
                userSelectionMap.put(user, false);
            }

            System.out.println("Filtered users added to table: " + filteredUsers);
        }
        else {
            System.out.println("No users retrieved or an error occurred.");
        }
    }

    private void saveRolesOnServer(Map<User, Boolean> userSelectionMap) {
        List<User> selectedUsers = new ArrayList<>();

        for (Map.Entry<User, Boolean> entry : userSelectionMap.entrySet()) {
            if (entry.getValue()) {
                selectedUsers.add(entry.getKey());
            }
        }
        List<Integer> selectedPersonIds = selectedUsers.stream()
                .map(user -> user.getPerson().getPersonId())
                .collect(Collectors.toList());
        if (!selectedPersonIds.isEmpty())
        {
            System.out.println("Selected Person IDs: " + selectedPersonIds);
            boolean checker = sendPersonIdsToServer(selectedPersonIds, RequestType.GIVE_ROLE);
            if (checker)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешная выдача ролей");
                successAlert.setHeaderText("Роли выданы успешно");
                successAlert.setContentText("Роли для выбранных пользователей были успешно обновлены.");
                successAlert.showAndWait();

                hideAllPanels();
                startPanel.setVisible(true);
           }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Не удалось выдать роли");
                errorAlert.setContentText("Произошла ошибка" );
                errorAlert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText("Не выбраны пользователи");
            alert.setContentText("Не удалось найти пользователей для выдачи роли.");
            alert.initOwner(giveRolePanel.getScene().getWindow());
            alert.showAndWait();
        }
    }

    private void handleDeleteWorker() {
        hideAllPanels();
        deleteWorkerPanel.setVisible(true);
        System.out.println("Requesting all workers...");

        List<User> workers = getUsersFromServer();
        if (!workers.isEmpty()) {
            List<User> filteredWorkers = workers.stream()
                    .filter(worker -> worker.getPerson() != null
                            && worker.getPerson().getRole() != null
                            && "WORKER".equalsIgnoreCase(worker.getPerson().getRole().getRoleName().toString()))
                    .collect(Collectors.toList());

            ObservableList<User> workerObservableList = FXCollections.observableArrayList(filteredWorkers);
            workerTable.setItems(workerObservableList);

            for (User worker : filteredWorkers) {
                workerSelectionMap.put(worker, false);
            }
            System.out.println("Filtered workers added to table: " + filteredWorkers);
        } else {
            System.out.println("No workers retrieved or an error occurred.");
        }
    }

    private void deleteWorkersOnServer(Map<User, Boolean> workerSelectionMap)
    {
        List<User> selectedWorkers = new ArrayList<>();

        for (Map.Entry<User, Boolean> entry : workerSelectionMap.entrySet()) {
            if (entry.getValue()) {
                selectedWorkers.add(entry.getKey());
            }
        }
        List<Integer> selectedPersonIds = selectedWorkers.stream()
                .map(user -> user.getPerson().getPersonId())
                .collect(Collectors.toList());
        if (!selectedPersonIds.isEmpty())
        {
            System.out.println("Selected Person IDs: " + selectedPersonIds);
            boolean checker = sendPersonIdsToServer(selectedPersonIds, RequestType.DELETE_WORKER);
            if (checker)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешное увольнение");
                successAlert.setHeaderText("Выбранные работники уволены");
                successAlert.setContentText("Выбранные работники успешно уволены и удалены из системы.");
                successAlert.showAndWait();

                hideAllPanels();
                startPanel.setVisible(true);
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Не удалось выдать роли");
                errorAlert.setContentText("Произошла ошибка" );
                errorAlert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText("Не выбраны работники");
            alert.setContentText("Не удалось уволить работника.");
            alert.initOwner(giveRolePanel.getScene().getWindow());
            alert.showAndWait();
        }
    }
    private void handleAboutUs() {
        showAboutCompanyPanel();
        System.out.println("About Us clicked.");
    }

    private void openAuthorizationWindow() {
        try {
            SceneSwitcher.switchSceneStart("authorization.fxml", profile, "Authorization");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить authorization.fxml");
        }
        System.out.println("switch to authorization");
    }

    public List<User> getUsersFromServer() {
        List<User> users = new ArrayList<>();

        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(RequestType.GET_USERS);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = new Gson().fromJson(responseJson, Response.class);

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully retrieved users.");
                    Type userListType = new TypeToken<List<User>>() {}.getType();
                    users = new Gson().fromJson(new Gson().toJson(response.getData()), userListType);
                } else {
                    System.out.println("Failed to retrieve users: " + response.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
    private boolean sendPersonIdsToServer(List<Integer> personIds, RequestType type) {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(personIds));
        request.setRequestType(type);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = new Gson().fromJson(responseJson, Response.class);
                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully sent person IDs to server.");
                    return true;
                } else {
                    System.out.println("Failed to send person IDs: " + response.getMessage());
                    return false;
                }
            } else {
                System.out.println("Received null response from server.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while sending person IDs to server.");
        }
        return false;
    }
}