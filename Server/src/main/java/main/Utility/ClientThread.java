package main.Utility;

import com.google.gson.Gson;
import main.Enums.ResponseStatus;
import main.Models.Entities.Person;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Services.PersonService;
import main.Services.RoleService;
import main.Services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket clientSocket;
    private Request request;
    private Gson gson;
    private BufferedReader in;
    private PrintWriter out;

    private PersonService personService = new PersonService();
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();

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
                                response = new Response(ResponseStatus.OK, "User is registered", user.getPerson().getRole().getRoleName());
                        } catch (Exception e) {
                            response = new Response(ResponseStatus.ERROR, "Error in registration: " + e.getMessage(), null);
                        }
                        break;
                    }
                    case LOGIN: {
                        Person person = new Gson().fromJson(request.getRequestMessage(), Person.class);

                        Integer roleId = personService.findRoleIdByLoginAndPassword(person);
                        response = new Response();

                        if (roleId != null) {
                            response.setResponseStatus(ResponseStatus.OK);
                            response.setData(roleId);
                        } else {
                            response.setResponseStatus(ResponseStatus.ERROR);
                            response.setData("user not found");
                        }

                        out.println(new Gson().toJson(response));
                        out.flush();
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