package server.src.main.java.org.example;


import commands.Command;
import commands.HelpCommand;
import helpers.CollectionManager;
import helpers.Invoker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket;
    private BufferedOutputStream bfWriter;
    private BufferedInputStream bfReader;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public Command receiveCommand(Socket socket) throws IOException {
        Command command = null;

        ois = new ObjectInputStream(socket.getInputStream());
        try {
            command = (Command) ois.readObject();
        }
        catch (EOFException e) {

            System.out.println("Клиент отключился");
            throw new EOFException();
        }
        finally {
            return command;
        }
    }

    public void sendMessage(Socket socket, Command command) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(command);
        oos.flush();

    }
    public void processCommand(Socket socket) throws IOException {
        try {
            while (true) {
                Command command = receiveCommand(socket);
                if (command == null) {
                    continue;
                }
                System.out.println(command);
                sendMessage(socket, command);
            }
        }
        catch (EOFException e){
            throw new EOFException();
        }
    }




    public void connect() throws IOException {
        Socket socket = serverSocket.accept();
        bfWriter = new BufferedOutputStream(socket.getOutputStream());
        bfWriter.write(("Здравствуйте, " + System.getProperty("user.name") + "!\r\n").getBytes());
        bfWriter.flush();
        CollectionManager cm = new CollectionManager();
        Invoker invoker = new Invoker(cm);

        while (true){
            try {
                processCommand(socket);
            }
            catch (EOFException e){
                System.out.println("Клиент отключился");
                break;

            }

        }



    }
    public void processObjects(){}
    public static void main(String[] args) throws IOException {
        Server server = new Server(5578);
        System.out.println("blbablablab");
        server.connect();


    }
}
