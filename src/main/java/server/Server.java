package server;

import classes.SpaceMarine;
import commands.Command;
import helpers.CollectionManager;
import helpers.Invoker;
import request.Request;
import response.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NavigableSet;
import java.util.Scanner;

public class Server {
    ServerSocket serverSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    Socket socket;
    CollectionManager cm;

    Invoker invoker;
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        cm = new CollectionManager();
        invoker = new Invoker(cm);
    }
    public void sendObject(Socket socket, String message) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        Response response = new Response(message);
        oos.writeObject(response);
        System.out.println("Sent");
        oos.flush();
        oos.close();
    }
    public String receiveObject(Socket socket) {
        String response = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldSystemOut = System.out;
        System.setOut(ps);

        try {
            ois = new ObjectInputStream(socket.getInputStream());

            Request request = (Request) ois.readObject();
            String[] args = request.getArgs();

            if (request.getSpaceMarine() != null) {
                Command command = invoker.getCommands().get(request.getArgs()[0]);
                command.passSpaceMarine(request.getSpaceMarine());
                invoker.runCommand(command);
            }
            else {
                invoker.runCommand(args);
            }
            System.out.flush();

            System.setOut(oldSystemOut);
            response = baos.toString();


            System.out.println("Received");
        } catch (IOException e) {
            System.out.println("Error, server didn't receive anything");
//            receiveObject(socket);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            return response;
        }
    }
    public void connect() throws IOException {
        socket = serverSocket.accept();
    }

    public void handleClient() throws IOException, ClassNotFoundException {
        try {
            String message = receiveObject(socket);
            sendObject(socket, message);
        }
        catch (Exception e) {

            System.out.println("Клиент отключился");
        }
    }
    public void save(){
        cm.save(cm.spaceMarines);
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server(3478);
        try {
            while (!server.serverSocket.isClosed()) {
                server.connect();
                server.handleClient();
                server.save();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
