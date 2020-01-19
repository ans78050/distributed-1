package server;

import common.Log;
import common.SampleRsa;
import common.command.Command;
import protocol.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

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

            KeyPair keyPair = SampleRsa.buildKeyPair();
            PublicKey pubKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] pubKeyByte = pubKey.getEncoded();
            output.writeInt(pubKeyByte.length);
            output.write(pubKeyByte, 0, pubKeyByte.length);

            int len = input.readInt();
            byte[] msg = new byte[len];
            input.read(msg, 0, len);
            msg = SampleRsa.decrypt2(privateKey, msg);
            String s = new String(msg);

            //String s = input.readUTF();
            Log.i("Worker receive: " + s);
            String[] s2 = s.split("/");
            String[] s3 = s2[1].split(",");
            int userId = Integer.parseInt(s3[0]);
            String userType = s3[1];
            Command command = getCommand(s2[0]);
            String resMsg;
            if (command == null) {
                resMsg = "" + Response.STATUS_SERVER_ERROR;
            } else if (command.authorizationLevel() == Command.AUTHORIZATION_LEVEL_ADMIN && !userType.equals("admin")) {
                resMsg = "" + Response.STATUS_AUTHENTICATION_FAIL;
            } else {
                Response response = command.exec(database);
                resMsg = response.getStatus() + "\n" + response.getBody();
            }
            byte[] b = resMsg.getBytes();
            output.writeInt(b.length);
            output.write(b, 0, b.length);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
