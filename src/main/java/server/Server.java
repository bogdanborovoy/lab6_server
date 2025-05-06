package server;

import classes.SpaceMarine;
import commands.Command;
import commands.HelpCommand;
import commands.ShowCommand;
import helpers.CollectionManager;
import helpers.Invoker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
    private static final Logger logger = LogManager.getLogger(Server.class);
    Scanner scanner;

    Invoker invoker;
    public Server(int port) throws IOException {
        scanner = new Scanner(System.in);
        serverSocket = new ServerSocket(port);
        cm = new CollectionManager();
        invoker = new Invoker(cm);

    }
    public void sendObject(Socket socket, String message) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        Response response = new Response(message);
        oos.writeObject(response);
        logger.info("Отправление ответа пользователю");
        oos.flush();
        oos.close();
    }
    public String receiveObject(Socket socket) {
        String response = "";
        logger.info("Обработка команды от пользователя");
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



        } catch (IOException e) {
            logger.info("Конец потока данных");
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
        logger.info("Подключился пользователь");
    }

    public void handleClient() throws IOException, ClassNotFoundException {
        try {
            String message = receiveObject(socket);
            sendObject(socket, message);
            if (scanner.hasNextLine()) {
                save();
                System.exit(0);
            }
        }
        catch (Exception e) {

            logger.info("Пользователь отключился");
        }
    }
    public void save(){
        cm.save(cm.spaceMarines);
    }
    public static void main(String[] args) throws IOException {

        int port = 2505;
        Server server = new Server(port);
        logger.info("Начало работы сервера");


        try {
            while (true) {
                server.connect();
                server.handleClient();
                server.save();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
