package main.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.enums.RequestType;
import main.enums.ResponseStatus;
import main.enums.TestDriveStatus;
import main.models.adapter.LocalDateTimeAdapter;
import main.models.entities.*;
import main.models.strategy.DeleteContext;
import main.models.strategy.DeleteStrategy;
import main.models.strategy.FailureStrategy;
import main.models.strategy.SuccessStrategy;
import main.models.tcp.Request;
import main.models.tcp.Response;
import main.utility.ClientSocket;
import main.utility.SceneSwitcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static main.enums.RequestCarStatus.NONE;
import static main.enums.RequestType.*;

public class UserPageController {
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
    private AnchorPane favoritesPanel;
    @FXML
    private AnchorPane checkMyTestDrivesPanel;
    @FXML
    private AnchorPane checkRequestsPanel;
    @FXML
    private AnchorPane viewCarsPanel;

    @FXML
    private Button aboutUs;

    @FXML
    private VBox carsContainer;
    @FXML
    private VBox requestsContainer;
    @FXML
    private VBox testDrivesContainer;
    @FXML
    private VBox favoritesContainer;
    @FXML
    private Label header;

    @FXML
    private HBox  favoriteCheck;
    @FXML
    private HBox myRequests;
    @FXML
    private HBox checkMyTestDrives;

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
    public void initialize()
    {
        aboutUs.setOnMouseClicked(event -> handleAboutUs());
        favoriteCheck.setOnMouseClicked(event -> handleFavoriteCheck());
        myRequests.setOnMouseClicked(event -> handleMyRequests());
        initProfileHandler();
        checkCar.setOnAction(event -> handleCarSelection());
        checkCar.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(checkCar.getPromptText());
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
        checkMyTestDrives.setOnMouseClicked(event -> handleCheckTestDrives());
    }

    //top panel
    private void handleAboutUs()
    {
        hideAllPanels();
        aboutUsPanel.setVisible(true);
        developerPane.setVisible(true);
        aboutCompanyPane.setVisible(true);
        connectionPane.setVisible(true);
        System.out.println("About Us clicked.");
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

    //display cars
    private void handleCarSelection()
    {
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
        styleButton(detailsButton);
        detailsButton.setLayoutX(10);
        detailsButton.setLayoutY(370);
        carPane.getChildren().add(detailsButton);

        Button testDriveButton = new Button("Записаться на тест-драйв");
        styleButton(testDriveButton);
        testDriveButton.setLayoutX(200);
        testDriveButton.setLayoutY(370);
        carPane.getChildren().add(testDriveButton);

        Button applyButton = new Button("Оформить заявку");
        styleButton(applyButton);
        applyButton.setLayoutX(10);
        applyButton.setLayoutY(410);
        carPane.getChildren().add(applyButton);

        Button favoriteButton = new Button("Добавить в избранное");
        styleButton(favoriteButton);
        favoriteButton.setLayoutX(200);
        favoriteButton.setLayoutY(410);
        carPane.getChildren().add(favoriteButton);

        detailsButton.setOnAction(event -> showCarDetailsModal(car));
        applyButton.setOnAction(event -> handleApplyRequest(car));
        testDriveButton.setOnAction(event -> handleTestDriveBooking(car));
        favoriteButton.setOnAction(event -> handleAddFavorite(car));
        return carPane;
    }

    //buttons on car card
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
    private void handleApplyRequest(Car car)
    {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Подтверждение заявки");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Вы уверены, что хотите оформить заявку на этот автомобиль?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Вы должны быть авторизованы для оформления заявки!");
                errorAlert.showAndWait();
                return;
            }

            CarRequest carRequest = new CarRequest(currentUser,car,NONE);
            boolean checker = sendRequestToServer(carRequest);
            if (checker) {
                showSuccessAlert("Успешная установка статуса", "Успешно оставлена заявка");
            } else {
                showAlert("Ошибка", "Произошла ошибка при оставлении заявки.");
            }
        }
    }
    private void handleAddFavorite(Car car)
    {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Подтверждение избранного");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Вы уверены, что хотите добавить этот автомобиль в избранное?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            User currentUser = Session.getUser();
            if (currentUser == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Вы должны быть авторизованы для оформления заявки!");
                errorAlert.showAndWait();
                return;
            }

            Favorite favorite = new Favorite(currentUser,car);
            boolean checker = sendFavoriteToServer(favorite,ADD_FAVORITE);
            if (checker) {
                showSuccessAlert("Успешное добавление", "Успешно добавлено в избранное");
            } else {
                showAlert("Ошибка", "Произошла ошибка при добавлении в избранное.");
            }
        }
    }
    private void handleTestDriveBooking(Car car)
    {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Запись на тест-драйв");

        VBox modalContainer = new VBox(10);
        modalContainer.setPadding(new Insets(20));
        modalContainer.setStyle("-fx-background-color: #282828; -fx-border-radius: 10; -fx-background-radius: 10;");
        modalContainer.setAlignment(Pos.CENTER);

        Label headerLabel = new Label("Выберите дату и время для тест-драйва");
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-font-size: 14px;");
        datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now().plusDays(7))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }
            }
        });

        TextField timeField = new TextField();
        timeField.setPromptText("Введите время (формат: 00:00)");
        timeField.setStyle("-fx-font-size: 14px;");

        Button bookButton = new Button("Записаться");
        bookButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        bookButton.setOnAction(e -> {
            if (datePicker.getValue() == null || timeField.getText().trim().isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Пожалуйста, заполните все поля!");
                errorAlert.showAndWait();
                return;
            }

            String timePattern = "^([01]\\d|2[0-3]):[0-5]\\d$";
            if (!timeField.getText().matches(timePattern)) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Неправильный формат времени! Введите время в формате 00:00.");
                errorAlert.showAndWait();
                timeField.clear();
                return;
            }
            LocalDate selectedDate = datePicker.getValue();
            String time = timeField.getText();


            LocalDateTime driveDate = combineDateAndTime(selectedDate,time);
            TestDrive testDrive = new TestDrive(car,Session.getUser(),driveDate,TestDriveStatus.NONE);

            boolean checker = sendTestDriveToServer(testDrive, SAVE_TEST_DRIVE);
            if (checker) {
                showSuccessAlert("Успешная запись", "Успешная запись на тест драйв");
                modalStage.close();
            } else {
                showAlert("Ошибка", "Произошла ошибка при записи.");
            }
        });

        modalContainer.getChildren().addAll(headerLabel, datePicker, timeField, bookButton);

        Scene modalScene = new Scene(modalContainer, 400, 300);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    //display favorites
    private void  handleFavoriteCheck()
    {
        hideAllPanels();
        favoritesPanel.setVisible(true);
        List<Favorite> favorites = getFavoritesFromServer();
        List<Favorite> myFavorites = sortFavorites(favorites,Session.getUser().getUserId());
        displayFavorites(myFavorites);
        System.out.println("Favorites check clicked.");
    }
    private void displayFavorites(List<Favorite> favorites)
    {
        favoritesContainer.getChildren().clear();
        if (favorites== null || favorites.isEmpty()) {
            Label noDrivesLabel = new Label("Отсутствуют избранные");
            noDrivesLabel.setStyle("-fx-font-size: 24; -fx-text-fill: red;");
            noDrivesLabel.setAlignment(Pos.CENTER);
            favoritesContainer.getChildren().add(noDrivesLabel);
            return;
        }
        GridPane gridPane = new GridPane();
        gridPane.setHgap(250);
        gridPane.setVgap(50);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-padding: 20;");

        int column = 0;
        int row = 0;

        for (Favorite fav : favorites) {
            AnchorPane carPane = createFavoritesPane(fav);
            gridPane.add(carPane, column, row);

            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        favoritesContainer.getChildren().add(gridPane);
    }
    private AnchorPane createFavoritesPane(Favorite favorite)
    {
        Car car = favorite.getCar();
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
        styleButton(detailsButton);
        detailsButton.setLayoutX(10);
        detailsButton.setLayoutY(370);
        carPane.getChildren().add(detailsButton);

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setLayoutX(10);
        deleteButton.setLayoutY(410);
        carPane.getChildren().add(deleteButton);

        deleteButton.setOnAction(event -> {
            handleDeleteFavorite(favorite);
        });

        detailsButton.setOnAction(event -> showCarDetailsModal(car));
        return carPane;
    }
    private void handleDeleteFavorite(Favorite favorite)
    {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Вы уверены, что хотите убрать из избранного \"" + favorite.getCar().getName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Подтверждение удаления");
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            boolean checker = sendFavoriteToServer(favorite, DELETE_FAVORITE);
            if (checker) {
                showSuccessAlert("Успешное удаление", "Успешно удалено из избранное");
                List<Favorite> favorites = getFavoritesFromServer();
                List<Favorite> myFavorites = sortFavorites(favorites,Session.getUser().getUserId());
                displayFavorites(myFavorites);
            } else {
                showAlert("Ошибка", "Произошла ошибка при удалении избранное.");
            }
        }
    }


    //display car requests
    private void handleMyRequests()
    {
        hideAllPanels();
        checkRequestsPanel.setVisible(true);
        List<CarRequest> carRequests = getCarRequestsFromServer();
        List<CarRequest> myCarRequests = sortCarRequests(carRequests,Session.getUser().getUserId());
        displayCarRequests(myCarRequests);
        System.out.println("My Requests clicked.");
    }
    private void displayCarRequests(List<CarRequest> carRequests) {
        requestsContainer.getChildren().clear();
        if (carRequests == null || carRequests.isEmpty()) {
            Label noDrivesLabel = new Label("Отсутствуют заявки");
            noDrivesLabel.setStyle("-fx-font-size: 24; -fx-text-fill: red;");
            noDrivesLabel.setAlignment(Pos.CENTER);
            requestsContainer.getChildren().add(noDrivesLabel);
            return;
        }
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-padding: 20;");

        int column = 0;
        int row = 0;

        for (CarRequest carRequest : carRequests) {
            AnchorPane requestPane = createCarRequestPane(carRequest);
            gridPane.add(requestPane, column, row);

            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        requestsContainer.getChildren().add(gridPane);
    }

    private AnchorPane createCarRequestPane(CarRequest carRequest) {
        AnchorPane requestPane = new AnchorPane();
        requestPane.setPrefSize(400, 200);
        requestPane.setStyle("-fx-background-color: #555555; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label carLabel = new Label("Автомобиль: " + carRequest.getCar().getName());
        carLabel.setTextFill(Color.WHITE);
        carLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        carLabel.setLayoutX(10);
        carLabel.setLayoutY(10);
        requestPane.getChildren().add(carLabel);

        Label costLabel = new Label("Стоимость: " + carRequest.getCar().getCost());
        costLabel.setTextFill(Color.WHITE);
        costLabel.setStyle("-fx-font-size: 16px;");
        costLabel.setLayoutX(10);
        costLabel.setLayoutY(50);
        requestPane.getChildren().add(costLabel);

        Label statusLabel = new Label("Статус: " + carRequest.getStatus());
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setStyle("-fx-font-size: 16px;");
        statusLabel.setLayoutX(10);
        statusLabel.setLayoutY(90);
        requestPane.getChildren().add(statusLabel);

        return requestPane;
    }


    //display users test drives
    private void handleCheckTestDrives()
    {
        hideAllPanels();
        checkMyTestDrivesPanel.setVisible(true);
        List<TestDrive> testDrives = getTestDrivesFromServer();
        List<TestDrive> myTestDrives = sortTestDrives(testDrives,Session.getUser().getUserId());
        displayUsersTestDrives(myTestDrives);
        System.out.println("check test drives clicked.");
    }
    private void displayUsersTestDrives(List<TestDrive> testDrives) {
        testDrivesContainer.getChildren().clear();

        if (testDrives == null || testDrives.isEmpty()) {
            Label noDrivesLabel = new Label("Отсутствуют тест-драйвы");
            noDrivesLabel.setStyle("-fx-font-size: 24; -fx-text-fill: red;");
            noDrivesLabel.setAlignment(Pos.CENTER);
            testDrivesContainer.getChildren().add(noDrivesLabel);
            return;
        }

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-padding: 20;");

        int column = 0;
        int row = 0;

        for (TestDrive testDrive : testDrives) {
            AnchorPane drivePane = createTestDrivePane(testDrive);
            gridPane.add(drivePane, column, row);

            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        testDrivesContainer.getChildren().add(gridPane);
    }
    private AnchorPane createTestDrivePane(TestDrive testDrive)
    {
        AnchorPane drivePane = new AnchorPane();
        drivePane.setPrefSize(400, 200);
        drivePane.setStyle("-fx-background-color: #444444; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label carNameLabel = new Label("Автомобиль: " + testDrive.getCar().getName());
        carNameLabel.setTextFill(Color.WHITE);
        carNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        carNameLabel.setLayoutX(10);
        carNameLabel.setLayoutY(10);
        drivePane.getChildren().add(carNameLabel);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        Label driveDateLabel = new Label("Дата и время: " + testDrive.getDriveDate().format(dateFormatter));
        driveDateLabel.setTextFill(Color.WHITE);
        driveDateLabel.setStyle("-fx-font-size: 16px;");
        driveDateLabel.setLayoutX(10);
        driveDateLabel.setLayoutY(50);
        drivePane.getChildren().add(driveDateLabel);

        Label statusLabel = new Label("Статус: " + testDrive.getStatus());
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setStyle("-fx-font-size: 16px;");
        statusLabel.setLayoutX(10);
        statusLabel.setLayoutY(90);
        drivePane.getChildren().add(statusLabel);

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setLayoutX(10);
        deleteButton.setLayoutY(130);
        drivePane.getChildren().add(deleteButton);

        deleteButton.setOnAction(event -> {
            handleDeleteTestDrive(testDrive);
        });

        return drivePane;
    }
    private void handleDeleteTestDrive(TestDrive testDrive)
    {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Вы уверены, что хотите удалить тест-драйв \"" + testDrive.getCar().getName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Подтверждение удаления");
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            boolean checker = sendTestDriveToServer(testDrive, DELETE_TEST_DRIVE);

            DeleteStrategy strategy = checker
                    ? new SuccessStrategy(() -> {
                List<TestDrive> updatedTestDrives = getTestDrivesFromServer();
                List<TestDrive> myTestDrives = sortTestDrives(updatedTestDrives, Session.getUser().getUserId());
                displayUsersTestDrives(myTestDrives);
            })
                    : new FailureStrategy();

            DeleteContext context = new DeleteContext(strategy);
            context.executeStrategy();

        }
    }


    //server
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
    public List<Favorite> getFavoritesFromServer()
    {
        List<Favorite> favorites = new ArrayList<>();

        Request request = new Request();
        request.setRequestMessage("");
        request.setRequestType(RequestType.GET_FAVORITES);
        try {
            ClientSocket.getInstance().getOut().println(new Gson().toJson(request));
            ClientSocket.getInstance().getOut().flush();

            String responseJson = ClientSocket.getInstance().getIn().readLine();
            if (responseJson != null) {
                System.out.println("Response from server: " + responseJson);
                Response response = new Gson().fromJson(responseJson, Response.class);

                if (response.getResponseStatus() == ResponseStatus.OK) {
                    System.out.println("Successfully retrieved favorites.");
                    Type favoritesListType = new TypeToken<List<Favorite>>() {}.getType();
                    favorites = new Gson().fromJson(new Gson().toJson(response.getData()), favoritesListType);
                } else {
                    System.out.println("Failed to retrieve favorites: " + response.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return favorites;
    }
    private boolean sendRequestToServer(CarRequest request)
    {
        Request req = new Request();
        req.setRequestMessage(new Gson().toJson(request));
        req.setRequestType(RequestType.REGISTRATE_REQUEST);
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
    private boolean sendFavoriteToServer(Favorite favorite,RequestType type)
    {
        Request req = new Request();
        req.setRequestMessage(new Gson().toJson(favorite));
        req.setRequestType(type);
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
    private boolean sendTestDriveToServer(TestDrive testDrive, RequestType type)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        Request req = new Request();
        req.setRequestMessage(gson.toJson(testDrive));
        req.setRequestType(type);

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

    //sorts
    private List<CarRequest> sortCarRequests(List<CarRequest> carRequests, int id)
    {
        List<CarRequest> sortedlist = new ArrayList<>();
        for (CarRequest item : carRequests) {
            if (item.getUser().getUserId() == id) {
                sortedlist .add(item);
            }
        }
        return sortedlist;
    }
    private List<Favorite> sortFavorites(List<Favorite> favorites, int id)
    {
        List<Favorite> sortedlist = new ArrayList<>();
        for (Favorite item : favorites) {
            if (item.getUser().getUserId() == id) {
                sortedlist .add(item);
            }
        }
        return sortedlist;
    }
    private List<TestDrive> sortTestDrives(List<TestDrive> testDrives, int id)
    {
        List<TestDrive> sortedlist = new ArrayList<>();
        for (TestDrive item : testDrives) {
            if (item.getUser().getUserId() == id) {
                sortedlist .add(item);
            }
        }
        return sortedlist;
    }


    //other
    private void hideAllPanels()
    {
        startPanel.setVisible(false);
        profilePanel.setVisible(false);
        myprofilePanel.setVisible(false);
        developerPane.setVisible(false);
        aboutCompanyPane.setVisible(false);
        connectionPane.setVisible(false);
        aboutUsPanel.setVisible(false);
        favoritesPanel.setVisible(false);
        checkMyTestDrivesPanel.setVisible(false);
        checkRequestsPanel.setVisible(false);
        viewCarsPanel.setVisible(false);
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
    public static LocalDateTime combineDateAndTime(LocalDate selectedDate, String time)
    {
        try {
            LocalTime localTime = LocalTime.parse(time);

            return LocalDateTime.of(selectedDate, localTime);
        } catch (DateTimeParseException e) {
            System.err.println("Ошибка в формате времени: " + e.getMessage());
            return null;
        }
    }
    private void styleButton(Button button)
    {
        button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 5 10;");
    }


}
