package main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.models.entities.*;
import main.models.factories.CarCellFactory;
import main.models.factories.CarCellFactoryImpl;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static main.enums.RequestType.ADD_CAR;
import static main.enums.RequestType.DELETE_CAR;

public class AdminPageController {
    @FXML
    private AnchorPane startPanel;
    @FXML
    private AnchorPane deleteCarsPanel;
    @FXML
    private AnchorPane addCarPanel;
    @FXML
    private AnchorPane addCarForm;
    @FXML
    private AnchorPane giveRolePanel;
    @FXML
    private AnchorPane deleteWorkerPanel;
    @FXML
    private AnchorPane aboutUsPanel;
    @FXML
    private AnchorPane viewCarsPanel;
    @FXML
    private AnchorPane myprofilePanel;
    @FXML
    private AnchorPane profilePanel;
    @FXML
    private AnchorPane developerPane;
    @FXML
    private AnchorPane aboutCompanyPane;
    @FXML
    private AnchorPane connectionPane;

    @FXML
    private HBox addCar;
    @FXML
    private HBox giveRole;
    @FXML
    private HBox deleteWorker;
    @FXML
    private HBox deleteCar;

    @FXML
    private VBox carsContainer;

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
    private Label header;

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
    @FXML
    private Button selectFileButton;

    @FXML
    private Button addCarToSystem;
    @FXML
    private TextField brand;
    @FXML
    private TextField power;
    @FXML
    private TextField cost;
    @FXML
    private TextField maxSpeed;
    @FXML
    private ComboBox<String> petrolType;
    @FXML
    private ComboBox<String> carType;

    @FXML
    private TableView<Car> deleteCarsTable;
    @FXML
    private TableColumn<Car, String> carNameColumn;
    @FXML
    private TableColumn<Car, String> carTypeColumn;
    @FXML
    private TableColumn<Car, Double> maxSpeedColumn;
    @FXML
    private TableColumn<Car, String> petrolTypeColumn;
    @FXML
    private TableColumn<Car, Integer> carPowerColumn;
    @FXML
    private TableColumn<Car, Double> carCostColumn;
    @FXML
    private TableColumn<Car, Void> delButtonColumnCar;

    private Map<User, Boolean> userSelectionMap = new HashMap<>();
    private Map<User, Boolean> workerSelectionMap = new HashMap<>();
    private String selectedFilePath;

    @FXML
    public void initialize()
    {
        checkCar.setOnAction(event -> handleCarSelection());
        checkCar.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(profile.getPromptText());
                } else {
                    setText(item);
                }
            }
        });

        setButtonCellForComboBox(profile);
        setButtonCellForComboBox(petrolType);
        setButtonCellForComboBox(carType);
        setButtonCellForComboBox(checkCar);

        surnameColumn.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getName();
            String[] nameParts = fullName.split(" ", 3);
            String name = nameParts.length > 1 ? nameParts[0] : "";
            return new SimpleStringProperty(name);
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
        initializeDeleteCarsTable();
        selectFileButton.setOnAction(event -> openFileChooser());

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
    private <T> void setButtonCellForComboBox(ComboBox<T> comboBox)
    {
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(comboBox.getPromptText());
                } else {
                    setText(item.toString());
                }
            }
        });
    }
    private void initHandlers()
    {
        addCar.setOnMouseClicked(event -> handleAddCar());
        giveRole.setOnMouseClicked(event -> handleGiveRole());
        deleteWorker.setOnMouseClicked(event -> handleDeleteWorker());
        aboutUs.setOnMouseClicked(event -> handleAboutUs());
        sendNewRoles.setOnMouseClicked(event -> saveRolesOnServer( userSelectionMap));
        deleteWorkers.setOnMouseClicked(event -> deleteWorkersOnServer( workerSelectionMap));
        addCarToSystem.setOnMouseClicked(event -> handleAddCarToSystem());
        deleteCar.setOnMouseClicked(event ->  handleDeleteCarFromSystem());
    }

    //top panel
    private void showProfilePanel()
    {
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
    private void showLogoutConfirmation()
    {
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
    private void initProfileHandler()
    {
        profile.setOnAction(event -> {
            String selectedItem = profile.getValue();
            if ("Выход".equals(selectedItem)) {
                showLogoutConfirmation();
            } else if ("Мой профиль".equals(selectedItem)) {
                showProfilePanel();
                profile.setValue(null);
            }
        });
    }

    //delete car
    private void handleDeleteCarFromSystem()
    {
        hideAllPanels();
        deleteCarsPanel.setVisible(true);

        refreshCarTable();
        initializeDeleteCarsTable();
    }
    private void refreshCarTable()
    {
        List<Car> cars = getCarsFromServer();
        if (!cars.isEmpty()) {
            ObservableList<Car> carObservableList = FXCollections.observableArrayList(cars);
            deleteCarsTable.setItems(carObservableList);

            System.out.println("All cars added to table: " + cars);
        } else {
            System.out.println("No cars retrieved or an error occurred.");
        }
    }
    private void initializeDeleteCarsTable()
    {
        CarCellFactory carCellFactory = new CarCellFactoryImpl();
        carNameColumn.setCellValueFactory(cellData ->
                carCellFactory.createNameProperty(cellData.getValue()));

        carTypeColumn.setCellValueFactory(cellData ->
                carCellFactory.createTypeProperty(cellData.getValue()));

        maxSpeedColumn.setCellValueFactory(cellData ->
                carCellFactory.createMaxSpeedProperty(cellData.getValue()).asObject());

        petrolTypeColumn.setCellValueFactory(cellData ->
                carCellFactory.createPetrolTypeProperty(cellData.getValue()));

        carPowerColumn.setCellValueFactory(cellData ->
                carCellFactory.createPowerProperty(cellData.getValue()).asObject());

        carCostColumn.setCellValueFactory(cellData ->
                carCellFactory.createCostProperty(cellData.getValue()).asObject());
        delButtonColumnCar.setCellFactory(column -> {
            return new TableCell<Car, Void>() {
                private final Button btn = new Button("Удалить");
                {
                    btn.setOnAction(e -> {
                        Car car = getTableView().getItems().get(getIndex());
                        boolean checker = sendCarToServer(car, DELETE_CAR);
                        if (checker) {
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Успешное удаление");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Автомобиль успешно удален из системы!");
                            successAlert.showAndWait();
                            refreshCarTable();
                        } else {
                            showAlert("Ошибка", "Произошла ошибка при удалении авто.");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        });
    }

    //add car
    private void handleAddCar()
    {
        hideAllPanels();
        addCarPanel.setVisible(true);
        addCarForm.setVisible(true);
    }
    private void handleAddCarToSystem()
    {
        String nameInput = brand.getText();
        String powerInput = power.getText();
        String costInput = cost.getText();
        String maxSpeedInput = maxSpeed.getText();

        String selectedPetrolType = petrolType.getValue();
        String selectedCarType = carType.getValue();

        boolean valid = true;

        valid = checkEmptyFields(nameInput, powerInput, costInput, maxSpeedInput, selectedPetrolType, selectedCarType);
        if(!valid)
        {
            showAlert("Ошибка ввода", "Заполните все поля");
        }
        else {
            int powerValue = 0;
            try {
                powerValue = Integer.parseInt(powerInput);
            } catch (NumberFormatException e) {
                power.clear();
                valid = false;
            }

            int maxSpeedValue = 0;
            try {
                maxSpeedValue = Integer.parseInt(maxSpeedInput);
            } catch (NumberFormatException e) {

                maxSpeed.clear();
                valid = false;
            }

            double costValue = 0;
            try {
                costValue = Double.parseDouble(costInput);
            } catch (NumberFormatException e) {
                cost.clear();
                valid = false;
            }

            if (valid) {
                CarType carType = new CarType();
                carType.setTypeName(selectedCarType);
                switch (selectedCarType) {
                    case "Легковая":
                        carType.setIdType(1);
                        break;
                    case "Внедорожник":
                        carType.setIdType(2);
                        break;
                    case "Купе":
                        carType.setIdType(3);
                        break;
                    case "Минивен":
                        carType.setIdType(4);
                        break;
                    default:
                        showAlert("Ошибка", "Неизвестный тип автомобиля.");
                        return;
                }
                Car car = new Car(maxSpeedValue,carType,selectedPetrolType,powerValue,nameInput,costValue,selectedFilePath);
                boolean checker = sendCarToServer(car, ADD_CAR);
                if (checker) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Успешное добавление");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Автомобиль успешно добавлен в систему!");
                    successAlert.showAndWait();

                } else {
                    showAlert("Ошибка", "Произошла ошибка при добавлении авто.");
                }
                clearFields();
            } else {
                showAlert("Ошибка ввода", "Очищены поля с некорректными данными");
            }
        }
    }
    private boolean checkEmptyFields(String brand, String power, String cost, String maxSpeed, String petrolType, String carType)
    {
        boolean valid = true;

        if (brand.isEmpty()) {
            this.brand.clear();
            valid = false;
        }
        if (power.isEmpty()) {
            this.power.clear();
            valid = false;
        }
        if (cost.isEmpty()) {
            this.cost.clear();
            valid = false;
        }
        if (maxSpeed.isEmpty()) {
            this.maxSpeed.clear();
            valid = false;
        }
        if (petrolType == null) {
            this.petrolType.getSelectionModel().clearSelection();
            valid = false;
        }
        if (carType == null) {
            this.carType.getSelectionModel().clearSelection();
            valid = false;
        }

        return valid;
    }

    //give role
    private void handleGiveRole()
    {
        hideAllPanels();
        giveRolePanel.setVisible(true);
        System.out.println("Requesting all users...");

        loadAndFilterUsers();
    }
    private void loadAndFilterUsers()
    {
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
        }
    }

    //delete worker
    private void handleDeleteWorker()
    {
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

    //about us
    private void handleAboutUs()
    {
        showAboutCompanyPanel();
        System.out.println("About Us clicked.");
    }
    private void showAboutCompanyPanel()
    {
        hideAllPanels();
        aboutUsPanel.setVisible(true);
        developerPane.setVisible(true);
        aboutCompanyPane.setVisible(true);
        connectionPane.setVisible(true);
    }

    //server
    private void saveRolesOnServer(Map<User, Boolean> userSelectionMap)
    {
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

                loadAndFilterUsers();
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
            boolean checker = sendPersonIdsToServer(selectedPersonIds, RequestType.DELETE_USER);
            if (checker)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешное увольнение");
                successAlert.setHeaderText("Выбранные работники уволены");
                successAlert.setContentText("Выбранные работники успешно уволены и удалены из системы.");
                successAlert.showAndWait();

                handleDeleteWorker();
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
    public List<User> getUsersFromServer()
    {
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
    public List<Car> getCarsFromServer()
    {
        List<Car> cars = new ArrayList<>();

        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(RequestType.GET_CARS);

        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = new Gson().fromJson(responseJson, Response.class);

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully retrieved cars.");
                    Type carListType = new TypeToken<List<Car>>() {}.getType();
                    cars = new Gson().fromJson(new Gson().toJson(response.getData()), carListType);
                } else {
                    System.out.println("Failed to retrieve cars: " + response.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cars;
    }
    private boolean sendPersonIdsToServer(List<Integer> personIds, RequestType type)
    {
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
    private boolean sendCarToServer(Car car, RequestType type)
    {
        Request request = new Request();
        request.setRequestMessage(new Gson().toJson(car));
        request.setRequestType(type);

        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
        ClientSocket.getInstance().getOut().flush();

        String serverResponse;
        try {
            serverResponse = ClientSocket.getInstance().getIn().readLine();
            if (serverResponse != null) {
                System.out.println("Response from server: " + serverResponse);
                Response response = new Gson().fromJson(serverResponse, Response.class);
                return response.getResponseStatus() == ResponseStatus.OK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //other
    private void openFileChooser()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Изображения (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(selectFileButton.getScene().getWindow());

        if (file != null) {
            selectedFilePath = file.getAbsolutePath();
            System.out.println(selectedFilePath);
        } else {
            System.out.println("file not choosen");
        }
    }
    private void hideAllPanels()
    {
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
        addCarPanel.setVisible(false);
        addCarForm.setVisible(false);
        deleteCarsPanel.setVisible(false);
    }
    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearFields()
    {
        brand.clear();
        power.clear();
        cost.clear();
        maxSpeed.clear();
        petrolType.getSelectionModel().clearSelection();
        carType.getSelectionModel().clearSelection();
        selectedFilePath = null;
    }


    //view cars
    private void handleCarSelection() {
        hideAllPanels();
        viewCarsPanel.setVisible(true);
        String selectedOption = checkCar.getValue();
        List<Car> cars = getCarsFromServer();
        List<Car> carsCopy = new ArrayList<>(cars);
        if ("Просмотр всех автомобилей".equals(selectedOption)) {
            header.setText("Наши автомобили");
            header.setTranslateX(40);
            displayCars(cars);
        } else if ("От самого дешевого к самому дорогому".equals(selectedOption)) {
            carsCopy.sort((car1, car2) -> Double.compare(car1.getCost(), car2.getCost()));
            header.setText("От самого дешевого к самому дорогому");
            header.setTranslateX(-40);
            displayCars(carsCopy);
        } else if ("От самого дорогого к самому дешевому".equals(selectedOption)) {
            carsCopy.sort((car1, car2) -> Double.compare(car2.getCost(), car1.getCost()));
            displayCars(carsCopy);
            header.setText("От самого дорогого к самому дешевому");
            header.setTranslateX(-40);
        }
        checkCar.setValue(null);
    }
    private void displayCars(List<Car> cars)
    {
        carsContainer.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(250);
        gridPane.setVgap(50);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-padding: 20;");

        int column = 0;
        int row = 0;

        for (Car car : cars) {
            AnchorPane carPane = createCarPane(car);
            gridPane.add(carPane, column, row);

            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        carsContainer.getChildren().add(gridPane);
    }
    private AnchorPane createCarPane(Car car)
    {
        AnchorPane carPane = new AnchorPane();
        carPane.setPrefSize(400, 450);
        carPane.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");

        AnchorPane.setTopAnchor(carPane, 50.0);
        AnchorPane.setLeftAnchor(carPane, (800 - carPane.getPrefWidth()) / 2);

        ImageView carImageView = new ImageView("file:///" + car.getImagePath());
        carImageView.setFitHeight(200);
        carImageView.setFitWidth(250);
        carImageView.setLayoutX((carPane.getPrefWidth() - carImageView.getFitWidth()) / 2);
        carImageView.setLayoutY(20);
        carPane.getChildren().add(carImageView);

        Label carNameLabel = new Label(car.getName());
        carNameLabel.setTextFill(Color.WHITE);
        carNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25px;");
        carNameLabel.setLayoutX(10);
        carNameLabel.setLayoutY(240);
        carPane.getChildren().add(carNameLabel);

        Label carCostLabel = new Label("Цена: " + car.getCost() + " $");
        carCostLabel.setTextFill(Color.WHITE);
        carCostLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        carCostLabel.setLayoutX(10);
        carCostLabel.setLayoutY(280);
        carPane.getChildren().add(carCostLabel);

        Label carTypeLabel = new Label("Тип: " + car.getCarType().getTypeName());
        carTypeLabel.setTextFill(Color.WHITE);
        carTypeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        carTypeLabel.setLayoutX(10);
        carTypeLabel.setLayoutY(320);
        carPane.getChildren().add(carTypeLabel);

        Button detailsButton = new Button("Подробная информация");
        detailsButton.setLayoutX(10);
        detailsButton.setLayoutY(370);
        carPane.getChildren().add(detailsButton);
        detailsButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 5 10;");

        detailsButton.setOnAction(event -> showCarDetailsModal(car));

        return carPane;
    }
    private void showCarDetailsModal(Car car)
    {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Подробная информация об автомобиле");

        VBox detailsContainer = new VBox(10);
        detailsContainer.setPadding(new Insets(20));
        detailsContainer.setStyle("-fx-background-color: #282828; -fx-border-radius: 10; -fx-background-radius: 10;");
        detailsContainer.setAlignment(Pos.CENTER_LEFT);


        Label nameLabel = new Label("Название: " + car.getName());
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label costLabel = new Label("Цена: " + car.getCost() + " $");
        costLabel.setTextFill(Color.WHITE);
        costLabel.setStyle("-fx-font-size: 14px;");

        Label typeLabel = new Label("Тип: " + car.getCarType().getTypeName());
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setStyle("-fx-font-size: 14px;");

        Label speedLabel = new Label("Максимальная скорость: " + car.getHighSpeed() + " км/ч");
        speedLabel.setTextFill(Color.WHITE);
        speedLabel.setStyle("-fx-font-size: 14px;");

        Label petrolLabel = new Label("Тип топлива: " + car.getPetrolType());
        petrolLabel.setTextFill(Color.WHITE);
        petrolLabel.setStyle("-fx-font-size: 14px;");

        Label powerLabel = new Label("Мощность: " + car.getPower() + " л.с.");
        powerLabel.setTextFill(Color.WHITE);
        powerLabel.setStyle("-fx-font-size: 14px;");

        detailsContainer.getChildren().addAll(
                nameLabel, costLabel, typeLabel, speedLabel,
                petrolLabel, powerLabel
        );

        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(event -> modalStage.close());
        closeButton.setAlignment(Pos.CENTER);

        VBox modalContent = new VBox(20, detailsContainer, closeButton);
        modalContent.setAlignment(Pos.CENTER);
        modalContent.setPadding(new Insets(20));

        Scene modalScene = new Scene(modalContent, 400, 400);
        modalStage.setScene(modalScene);

        modalStage.showAndWait();
    }

    //open window
    private void openAuthorizationWindow()
    {
        try {
            SceneSwitcher.switchSceneStart("authorization.fxml", profile, "Authorization");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить authorization.fxml");
        }
        System.out.println("switch to authorization");
    }
}