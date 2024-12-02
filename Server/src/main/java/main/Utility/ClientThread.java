package main.Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.Enums.ResponseStatus;
import main.Models.Entities.Admin;
import main.Models.Entities.Car;
import main.Models.Entities.Person;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Services.CarService;
import main.Services.PersonService;
import main.Services.RoleService;
import main.Services.UserService;
import org.hibernate.Hibernate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread implements Runnable {
    private Socket clientSocket;
    private Request request;
    private Gson gson;
    private BufferedReader in;
    private PrintWriter out;

    private PersonService personService = new PersonService();
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private CarService carService = new CarService();

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        gson = new Gson();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                String message = in.readLine();
                if (message == null) break;

                request = gson.fromJson(message, Request.class);
                System.out.println("Get request: " + message);
                Response response;

                switch (request.getRequestType()) {
                    case GET_USERS: {
                        List<User> users = userService.findAllEntities();
                        if (users != null) {
                            response = new Response(ResponseStatus.OK, "Users retrieved successfully", users);
                        } else {
                            response = new Response(ResponseStatus.ERROR, "No users found", null);
                        }
                        break;
                    }
                    case CHECK_PERSON: {
                        Person person = gson.fromJson(request.getRequestMessage(), Person.class);
                        try {
                            personService.saveEntity(person);
                            response = new Response(ResponseStatus.OK, "Person created", person.getPersonId());
                        } catch (RuntimeException e) {
                            response = new Response(ResponseStatus.ERROR, e.getMessage(), null);
                        }
                        break;
                    }
                    case SIGNUP: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        try {
                            userService.saveEntity(user);
                            response = new Response(ResponseStatus.OK, "User registered successfully", user);
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Error in registration: " + e.getMessage(), null);
                        }
                        break;
                    }
                    case LOGIN: {
                        Person person = new Gson().fromJson(request.getRequestMessage(), Person.class);

                        Person foundPerson = personService.findByLoginAndPassword(person);

                        if (foundPerson != null) {
                            Hibernate.initialize(foundPerson.getRole());
                            Object userOrAdmin = personService.findUserOrAdmin(foundPerson);
                            if (userOrAdmin != null) {
                                response = new Response(ResponseStatus.OK, "successful login", userOrAdmin);
                            } else {
                                response = new Response(ResponseStatus.ERROR, "user not found", null);
                            }
                        } else {
                            response = new Response(ResponseStatus.ERROR, "invalid login or password", null);
                        }
                        break;
                    }
                    case GIVE_ROLE: {
                        List<Integer> personIds = new Gson().fromJson(request.getRequestMessage(), new TypeToken<List<Integer>>(){}.getType());
                        try {
                            personService.updateRoles(personIds);
                            response = new Response(ResponseStatus.OK, "Roles updated successfully", null);
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Failed to update roles: " + e.getMessage(), null);
                        }
                        break;
                    }
                    case DELETE_WORKER: {
                        List<Integer> personIds = new Gson().fromJson(request.getRequestMessage(), new TypeToken<List<Integer>>(){}.getType());
                        try {
                            personService.deleteWorkers(personIds);
                            response = new Response(ResponseStatus.OK, "Workers deleted successfully", null);
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Failed to delete workers: " + e.getMessage(), null);
                        }
                        break;
                    }
                    case ADD_CAR: {
                        Car car = gson.fromJson(request.getRequestMessage(), Car.class);
                        try {
                            carService.saveEntity(car);
                            response = new Response(ResponseStatus.OK, "Car added successfully", car);
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Error in adding car: " + e.getMessage(), null);
                        }
                        break;
                    }
                    case GET_CARS: {
                        List<Car> cars = carService.findAllEntities();
                        if (cars != null) {
                            response = new Response(ResponseStatus.OK, "Cars retrieved successfully", cars);
                        } else {
                            response = new Response(ResponseStatus.ERROR, "No cars found", null);
                        }
                        break;
                    }
                    case DELETE_CAR:{
                        Car car = gson.fromJson(request.getRequestMessage(), Car.class);
                        try {
                            carService.deleteEntity(car);
                            response = new Response(ResponseStatus.OK, "Car deleted successfully", car);
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Error in deleting car: " + e.getMessage(), null);
                        }
                        break;
                    }
                    default: {
                        response = new Response(ResponseStatus.ERROR, "Unknown request type", null);
                        break;
                    }
                }

                out.println(gson.toJson(response));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}