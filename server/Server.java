package server;

import common.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    private static final boolean DO_IMPORT_DATA_FROM_FILE = false;

    public final static int PORT = 8080;

    private DatabaseUtility database;
    private ServerSocket serverSocket;

    private void init() throws ClassNotFoundException, IOException, SQLException {
        database = DatabaseUtility.connect();
        Log.i("Server connect Database -> done!");

        if(DO_IMPORT_DATA_FROM_FILE){                                   //Import Data
            Log.i("Server importing data -> start!");
            database.importDataFromFile();
            database.importDataStudent();
            database.importDataGrade();
            database.importDataSubject();
            database.importDataAdmin();
            Log.i("Server importing data -> done!");
        }

        serverSocket = new ServerSocket(PORT);                          //Create Socket
        Log.i("Server listen port: " + PORT + " -> done!");
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void listen() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Log.i("Server accept new Request");
            new Worker(clientSocket, database).start();
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.init();
            server.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
