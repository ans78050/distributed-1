package server;

import common.Log;
import common.command.Command;
import protocol.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker extends Thread {
    private Socket clientSocket;
    private DatabaseUtility database;

    private Command getCommand(String input) {
        return Command.fromCommandString(input);
    }

    public Worker(Socket clientSocket, DatabaseUtility database) {
        super();
        this.clientSocket = clientSocket;
        this.database = database;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            String s = input.readUTF();
            Log.i("Worker receive: " + s);
            String[] s2 = s.split("/");
            String[] s3 = s2[1].split(",");
            int userId = Integer.parseInt(s3[0]);
            String userType = s3[1];
            Command command = getCommand(s2[0]);
            if (command == null) {
                output.writeUTF("" + Response.STATUS_SERVER_ERROR);
                return;
            }
            if (command.authorizationLevel() == Command.AUTHORIZATION_LEVEL_ADMIN && !userType.equals("admin")) {
                output.writeUTF("" + Response.STATUS_AUTHENTICATION_FAIL);
                return;
            }
            Response response = command.exec(database);
            output.writeUTF(response.getStatus() + "\n" + response.getBody());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
