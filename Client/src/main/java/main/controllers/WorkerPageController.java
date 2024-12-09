package main.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.enums.RequestCarStatus;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.enums.TestDriveStatus;
import main.models.adapter.LocalDateTimeAdapter;
import main.models.entities.*;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkerPageController {
    @FXML
    private AnchorPane startPanel;
    @FXML
    private AnchorPane profilePanel;
    @FXML
    private AnchorPane aboutUsPanel;
    @FXML
    private AnchorPane developerPane;
    @FXML
    private AnchorPane aboutCompanyPane;
    @FXML
    private AnchorPane connectionPane;
    @FXML
    private AnchorPane myprofilePanel;
    @FXML
    private AnchorPane workingWithBidPanel;
    @FXML
    private AnchorPane checkBidPanel;
    @FXML
    private AnchorPane deleteUserPanel;
    @FXML
    private AnchorPane workingWithTestDrive;
    @FXML
    private AnchorPane viewCarsPanel;

    @FXML
    private HBox bidWorking;
    @FXML
    private HBox makeReport;
    @FXML
    private HBox deleteUser;
    @FXML
    private HBox checkRequests;
    @FXML
    private HBox testDriveFunc;

    @FXML
    private VBox carsContainer;

    @FXML
    private Label header;

    @FXML
    private Button aboutUs;

    @FXML
    private ComboBox<String> profile;
    @FXML
    private ComboBox<String> checkCar;
    @FXML
    private Label roleLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label FIOLabel;
    @FXML
    private Label birthDate;
    @FXML
    private Label passportNumber;
    @FXML
    private Label gmailLabel;

    @FXML
    private TableView<CarRequest> bidTable;
    @FXML
    private TableColumn<CarRequest, String> userNameColumn;
    @FXML
    private TableColumn<CarRequest, String> gmailUserColumn;
    @FXML
    private TableColumn<CarRequest, String> carNameColumn;
    @FXML
    private TableColumn<CarRequest, String> bidStatusColumn;
    @FXML
    private TableColumn<CarRequest, Void> changeStatusColumn;

    @FXML
    private TableView<TestDrive> testDriveTable;
    @FXML
    private TableColumn<TestDrive, String> userNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> gmailUserColumnTest;
    @FXML
    private TableColumn<TestDrive, String> carNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> testDriveStatusColumn;
    @FXML
    private TableColumn<TestDrive, String> testDriveDateColumn;
    @FXML
    private TableColumn<TestDrive, Void> changeStatusColumnTest;

    @FXML
    private TableView<CarRequest> bidCheckTable;
    @FXML
    private TableColumn<CarRequest, String> userNameColumnCheck;
    @FXML
    private TableColumn<CarRequest, String> gmailUserColumnCheck;
    @FXML
    private TableColumn<CarRequest, String> carNameColumnCheck;
    @FXML
    private TableColumn<CarRequest, String> bidStatusColumnCheck;

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
    private Button  deleteUserButton;

    private Map<User, Boolean> userSelectionMap = new HashMap<>();

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

        aboutUs.setOnMouseClicked(event -> handleAboutUs());
        initProfileHandler();
        initTestDriveTable();
        bidWorking.setOnMouseClicked(event -> handleWorkingWithBid());
        checkRequests.setOnMouseClicked(event -> handleCheckRequests());
        makeReport.setOnMouseClicked(event -> handleMakeReport());
        testDriveFunc.setOnMouseClicked(event -> handleWorkingWithTestDrive());
        deleteUser.setOnMouseClicked(event -> handleDeleteWorker());
        deleteUserButton.setOnMouseClicked(event -> deleteUsersOnServer(userSelectionMap));
        initBidWorkingTable();
        initBidTable();
        initUserDeleteTable();
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
        profile.setButtonCell(new ListCell<>() {
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
    }

    //delete worker
    private void handleDeleteWorker()
    {
        hideAllPanels();
        deleteUserPanel.setVisible(true);
        System.out.println("Requesting all workers...");

        List<User> users = getUsersFromServer();
        if (!users.isEmpty()) {
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getPerson() != null
                            && user.getPerson().getRole() != null
                            && "USER".equalsIgnoreCase(user.getPerson().getRole().getRoleName().toString()))
                    .collect(Collectors.toList());

            ObservableList<User> workerObservableList = FXCollections.observableArrayList(filteredUsers);
            userTable.setItems(workerObservableList);

            for (User user : filteredUsers) {
                userSelectionMap.put(user, false);
            }
            System.out.println("Filtered workers added to table: " + filteredUsers);
        } else {
            System.out.println("No workers retrieved or an error occurred.");
        }
    }
    private void initUserDeleteTable()
    {
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
    }


    //making report
    private void handleMakeReport()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение создания отчета");
        alert.setHeaderText("Вы уверены, что хотите создать отчет?");
        alert.setContentText("Нажмите ОК для создания или Отмена для возврата.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                List <CarRequest> carReq = getCarRequestsFromServer();
                boolean checker = saveAcceptedCarRequestsToFile(carReq);
                if (checker) {
                    showSuccessAlert("Успех", "Успешное создание отчета");
                } else {
                    showAlert("Ошибка", "Произошла ошибка при создании отчета.");
                }
            }
        });
    }

    //view about us
    private void handleAboutUs()
    {
        hideAllPanels();
        aboutUsPanel.setVisible(true);
        developerPane.setVisible(true);
        aboutCompanyPane.setVisible(true);
        connectionPane.setVisible(true);
        System.out.println("About Us clicked.");
    }

    //check requests
    private void handleCheckRequests()
    {
        hideAllPanels();
        checkBidPanel.setVisible(true);
        refreshCarRequestTable();
        System.out.println("Check requests clicked.");
    }
    private void refreshCarRequestTable()
    {
        List<CarRequest> carRequests = getCarRequestsFromServer();
        ObservableList<CarRequest> carRequestObservableList = FXCollections.observableArrayList(carRequests);
        bidCheckTable.setItems(carRequestObservableList);

        System.out.println("All requests added to the table: " + carRequests);
    }

    //working with bid
    private void handleWorkingWithBid()
    {
        hideAllPanels();
        workingWithBidPanel.setVisible(true);
        refreshCarRequestWorkingTable();
        System.out.println("Working with requests clicked.");
    }
    private void initBidTable()
    {
        userNameColumnCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getName()));
        gmailUserColumnCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getGmail()));
        carNameColumnCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getName()));
        bidStatusColumnCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }
    private void initBidWorkingTable()
    {
        userNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getName()));
        gmailUserColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getGmail()));
        carNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getName()));
        bidStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));
        bidStatusColumn.setCellFactory(column -> new TableCell<CarRequest, String>() {
            private final ComboBox<RequestCarStatus> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll(RequestCarStatus.values());
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    CarRequest request = getTableView().getItems().get(getIndex());
                    comboBox.setValue(request.getStatus());
                    comboBox.setOnAction(e -> {
                        request.setStatus(comboBox.getValue());
                    });

                    setGraphic(comboBox);
                }
            }
        });
        changeStatusColumn.setCellFactory(column -> new TableCell<CarRequest, Void>() {
            private final Button btn = new Button("Изменить");

            {
                btn.setOnAction(e -> {
                    CarRequest request = getTableView().getItems().get(getIndex());
                    RequestCarStatus selectedStatus = request.getStatus();
                    if (selectedStatus == null) {
                        showAlert("Ошибка", "Статус не может быть пустым.");
                        return;
                    }

                    boolean checker = sendRequestToServer(request);
                    if (checker) {
                        showSuccessAlert("Успешная установка статуса", "Статус изменен на " + selectedStatus);
                        refreshCarRequestWorkingTable();
                    } else {
                        showAlert("Ошибка", "Произошла ошибка при выдаче статуса.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
    private void refreshCarRequestWorkingTable()
    {
        List<CarRequest> carRequests = getCarRequestsFromServer();

        List<CarRequest> filteredRequests = carRequests.stream()
                .filter(request -> request.getStatus() == RequestCarStatus.NONE || request.getStatus() == RequestCarStatus.WAIT)
                .collect(Collectors.toList());

        if (!filteredRequests.isEmpty()) {
            ObservableList<CarRequest> carRequestObservableList = FXCollections.observableArrayList(filteredRequests);
            bidTable.setItems(carRequestObservableList);

            System.out.println("Filtered requests added to the table: " + filteredRequests);
        } else {
            bidTable.setItems(FXCollections.observableArrayList());
            System.out.println("No requests with status NONE or WAIT found.");
        }
    }

    //profile working
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

    //working with test drives
    private void handleWorkingWithTestDrive()
    {
        hideAllPanels();
        workingWithTestDrive.setVisible(true);
        refreshTestDriveTable();
        System.out.println("Working with test drives clicked.");
    }
    private void refreshTestDriveTable() {
        List<TestDrive> testDrives = getTestDrivesFromServer();
        LocalDate currentDate = LocalDate.now();
        LocalDate minDriveDate = currentDate.plusDays(7);
        LocalDateTime minDriveDateTime = minDriveDate.atStartOfDay();

        List<TestDrive> editableTestDrives = testDrives.stream()
                .filter(testDrive -> testDrive.getDriveDate().isAfter(minDriveDateTime) && "NONE".equals(testDrive.getStatus().toString()))
                .collect(Collectors.toList());

        List<TestDrive> readOnlyTestDrives = testDrives.stream()
                .filter(testDrive -> testDrive.getDriveDate().isAfter(minDriveDateTime) && !"NONE".equals(testDrive.getStatus().toString()))
                .collect(Collectors.toList());

        List<TestDrive> combinedTestDrives = new ArrayList<>(editableTestDrives);
        combinedTestDrives.addAll(readOnlyTestDrives);

        ObservableList<TestDrive> testDriveObservableList = FXCollections.observableArrayList(combinedTestDrives);
        testDriveTable.setItems(testDriveObservableList);

        System.out.println("Filtered test drives added to the table: " + combinedTestDrives);
    }

    private void initTestDriveTable() {
        userNameColumnTest.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getName()));
        gmailUserColumnTest.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getGmail()));
        carNameColumnTest.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getName()));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");

        testDriveDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDriveDate().format(dateFormatter)));
        testDriveStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        testDriveStatusColumn.setCellFactory(column -> new TableCell<TestDrive, String>() {
            private final ComboBox<TestDriveStatus> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll(TestDriveStatus.values());
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    TestDrive testDrive = getTableView().getItems().get(getIndex());
                    if ("NONE".equals(testDrive.getStatus().toString())) {
                        comboBox.setValue(testDrive.getStatus());
                        comboBox.setOnAction(e -> {
                            testDrive.setStatus(comboBox.getValue());
                        });
                        setGraphic(comboBox);
                    } else {
                        setText(testDrive.getStatus().toString());
                        setGraphic(null);
                    }
                }
            }
        });

        changeStatusColumnTest.setCellFactory(column -> new TableCell<TestDrive, Void>() {
            private final Button btn = new Button("Изменить");

            {
                btn.setOnAction(e -> {
                    TestDrive testDrive = getTableView().getItems().get(getIndex());
                    TestDriveStatus selectedStatus = testDrive.getStatus();
                    if (selectedStatus == null) {
                        showAlert("Ошибка", "Статус не может быть пустым.");
                        return;
                    }

                    boolean checker = sendTestDriveToServer(testDrive);
                    if (checker) {
                        showSuccessAlert("Успешная установка статуса", "Статус изменен на " + selectedStatus);
                        refreshTestDriveTable();
                    } else {
                        showAlert("Ошибка", "Произошла ошибка при выдаче статуса.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    //visual part
    private void showSuccessAlert(String title, String message)
    {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(title);
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }
    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void hideAllPanels()
    {
        startPanel.setVisible(false);
        profilePanel.setVisible(false);
        myprofilePanel.setVisible(false);
        developerPane.setVisible(false);
        aboutCompanyPane.setVisible(false);
        connectionPane.setVisible(false);
        aboutUsPanel.setVisible(false);
        workingWithBidPanel.setVisible(false);
        checkBidPanel.setVisible(false);
        deleteUserPanel.setVisible(false);
        workingWithTestDrive.setVisible(false);
        viewCarsPanel.setVisible(false);
    }
    private void showProfilePanel()
    {
        hideAllPanels();
        profilePanel.setVisible(true);
        myprofilePanel.setVisible(true);

        User user = Session.getUser();
        if (user != null) {
            roleLabel.setText(user.getPerson().getRole().getRoleName().toString());
            loginLabel.setText(user.getPerson().getLogin());
            FIOLabel.setText(user.getName());
            String formattedBirthDate = user.getBirthDate() + "." + user.getBirthMonth() + "." + user.getBirthYear();
            birthDate.setText(formattedBirthDate);
            passportNumber.setText(user.getPassportNum());
            gmailLabel.setText(user.getGmail());
        } else {
            System.out.println("No information about worker");
        }
    }

    //open windows
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

    //server part
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
    public boolean saveAcceptedCarRequestsToFile(List<CarRequest> carRequests)
    {
        String fileName = "report.txt";
        double totalCost = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (CarRequest request : carRequests) {
                if (request.getStatus() == RequestCarStatus.ACCEPT) {
                    Car car = request.getCar();
                    User user = request.getUser();
                    writer.write("ID заявки: " + request.getIdRequest());
                    writer.newLine();
                    writer.write("Имя клиента: " + user.getName());
                    writer.newLine();
                    writer.write("Номер паспорта клиента: " + user.getPassportNum());
                    writer.newLine();
                    writer.write("Почта клиента: " + user.getGmail());
                    writer.newLine();
                    writer.write("Название машины: " + car.getName());
                    writer.newLine();
                    writer.write("Тип машины: " + car.getCarType());
                    writer.newLine();
                    writer.write("Цена машины: " + car.getCost());
                    writer.newLine();
                    writer.write("----------------------------------------------");
                    writer.newLine();

                    totalCost += car.getCost();
                }
            }

            writer.newLine();
            writer.write("Полная стоимость всех машин из заявок: " + totalCost);
            writer.newLine();
            writer.flush();
            System.out.println("Data successfully written to " + fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<CarRequest> getCarRequestsFromServer()
    {
        List<CarRequest> carRequests = new ArrayList<>();

        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(RequestType.GET_CAR_REQUESTS);
        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = new Gson().fromJson(responseJson, Response.class);

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully retrieved car requests.");
                    Type carRequestListType = new TypeToken<List<CarRequest>>() {}.getType();
                    carRequests = new Gson().fromJson(new Gson().toJson(response.getData()), carRequestListType);
                } else {
                    System.out.println("Failed to retrieve car requests: " + response.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return carRequests;
    }
    public List<TestDrive> getTestDrivesFromServer()
    {
        List<TestDrive> testDrives = new ArrayList<>();

        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(RequestType.GET_TEST_DRIVES);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        try {
            ClientSocket.getInstance().getOut().println(gson.toJson(request));
            ClientSocket.getInstance().getOut().flush();

            // Получение ответа от сервера
            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = gson.fromJson(responseJson, Response.class);

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully retrieved test drives.");

                    Type testDriveListType = new TypeToken<List<TestDrive>>() {}.getType();

                    Object dataObject = response.getData();
                    String dataJson;

                    if (dataObject instanceof String) {
                        dataJson = (String) dataObject;
                    } else {
                        dataJson = gson.toJson(dataObject);
                    }

                    testDrives = gson.fromJson(dataJson, testDriveListType);
                } else {
                    System.out.println("Failed to retrieve test drives: " + response.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testDrives;
    }
    private void deleteUsersOnServer(Map<User, Boolean> usersSelectionMap)
    {
        List<User> selectedUsers = new ArrayList<>();

        for (Map.Entry<User, Boolean> entry : usersSelectionMap.entrySet()) {
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
            boolean checker = sendPersonIdsToServer(selectedPersonIds, RequestType.DELETE_USER);
            if (checker)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешное удаление");
                successAlert.setHeaderText("Выбранные пользователи удалены");
                successAlert.setContentText("Выбранные пользователи удалены из системы.");
                successAlert.showAndWait();

                handleDeleteWorker();
            }
            else
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Не удалось удалить");
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
            alert.showAndWait();
        }
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
    private boolean sendRequestToServer(CarRequest request)
    {
        Request req = new Request();
        req.setRequestMessage(new Gson().toJson(request));
        req.setRequestType(RequestType.SET_REQUEST_STATUS);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(req));
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
    private boolean sendTestDriveToServer(TestDrive testDrive)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        Request req = new Request();
        req.setRequestMessage(gson.toJson(testDrive));
        req.setRequestType(RequestType.SET_TEST_DRIVE_STATUS);

        ClientSocket.getInstance().getOut().println(gson.toJson(req));
        ClientSocket.getInstance().getOut().flush();

        String serverResponse;
        try {
            serverResponse = ClientSocket.getInstance().getIn().readLine();
            if (serverResponse != null) {
                System.out.println("Response from server: " + serverResponse);
                Response response = gson.fromJson(serverResponse, Response.class);
                return response.getResponseStatus() == ResponseStatus.OK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    //all for view cars
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
}
