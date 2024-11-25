package main.utility;

import main.models.entities.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private User user;
    private static Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static final ClientSocket SINGLE_INSTANCE = new ClientSocket();

    private ClientSocket() {
        try{
            socket = new Socket("localhost", 3010);
            in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        }catch (Exception e){}
    }


    public static ClientSocket getInstance(){return SINGLE_INSTANCE;}

    public Socket getSocket(){return socket;}

    public BufferedReader getIn(){return in;}
    public PrintWriter getOut(){return out;}
    public User getUser(){return user;}
    public void setUser(User user){this.user = user;}
}
