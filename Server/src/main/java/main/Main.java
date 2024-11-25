package main;

import main.Models.Entities.User;
import main.Services.UserService;
import main.Utility.ClientThread;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final int PORT_NUMBER = 3010;
    private static ServerSocket serverSocket;

    private static ClientThread clientHandler;
    private static Thread thread;
    private static List<Socket> currentSockets = new ArrayList<Socket>();
    public static void main(String[] args) throws IOException {


        serverSocket = new ServerSocket(PORT_NUMBER);
        while(true) {
            for(Socket socket : currentSockets) {
                if(socket.isClosed()){
                    currentSockets.remove(socket);
                    continue;
                }
                String socketInfo = "Client: " + socket.getInetAddress()+":"+socket.getPort();
                System.out.println(socketInfo);
            }
            Socket socket = serverSocket.accept();
            currentSockets.add(socket);
            clientHandler=new ClientThread(socket);
            thread= new Thread(clientHandler);
            thread.start();
            System.out.flush();
        }
    }
}