package client;

import common.JsonHelper;
import common.Log;
import common.SampleRsa;
import common.Tablable;
import common.command.*;
import common.display.Table;
import common.model.*;
import protocol.Request;
import protocol.Response;
import server.DatabaseUtility;
import server.Server;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class Client {

    public Response request(Request request) throws IOException, InvalidKeyException {
        Socket socket = createSocket(request);                     //Socket Create
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        /////////////////////////////////////////////////////////////
        //Encrypt by using PubKey
        /////////////////////////////////////////////////////////////

        // A: receive public key from server
        // A1
        int pubKeyByteLength = input.readInt();
        byte[] pubKeyByte = new byte[pubKeyByteLength];
        // A2
        input.read(pubKeyByte, 0, pubKeyByteLength);
        PublicKey pubKey = new RSAPublicKeyImpl(pubKeyByte);

        // Encrypt Message
        byte[] msg = new byte[0];
        try {
            msg = SampleRsa.encrypt2(pubKey, request.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // B: send message to server
        // B1
        output.writeInt(msg.length);
        // B2
        output.write(msg, 0, msg.length);

        // C: receive result from server
        // C1
        int resLength = input.readInt();
        byte[] b = new byte[resLength];
        // C2
        input.read(b, 0, resLength);
        String res = new String(b);
        socket.close();
/////////////////////////////////////////////////////////////
        Log.i("Client receive res = " + res);

        int firstLineEndAt = res.indexOf("\n");
        String firstLine;
        firstLine = res.substring(0, firstLineEndAt);
        int status = Integer.parseInt(firstLine);
        String body = res.substring(firstLineEndAt + 1);
        return new Response(status, body);
    }

    private Socket createSocket(Request request) throws IOException {
        return new Socket(request.getHost(), request.getPort());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        Users currentUser = null;
        boolean autoLoginAfterRegister = true;

        while (true) {

            while (currentUser == null) {
                System.out.println("System need Login, type 0 for register, or leave blank to exit");
                System.out.print("userId: ");
                String userId = scanner.nextLine();
                if (userId.isEmpty()) {
                    System.out.println("exit --> bye!");
                    break;
                } else if (userId.equals("0")) {
                    System.out.print("type: ");
                    String type = scanner.nextLine();
                    System.out.print("username: ");
                    String username = scanner.nextLine();
                    System.out.print("password: ");
                    String password = scanner.nextLine();
                    try {
                        Command cmd = new RegisterCommand(username, type, password);
                        Request request = new Request("localhost", Server.PORT, cmd.toCommandString(false));
                        Response response = client.request(request);

                        if (response.getStatus() == Response.STATUS_SERVER_ERROR) {
                            System.out.print(response.getBody() + "\n\n");
                            continue;
                        }

                        if (autoLoginAfterRegister) {
                            currentUser = new Users();
                            currentUser.deserialize(response.getBody());
                        }
                        continue;
                    } catch (IOException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }

                System.out.print("password: ");
                String password = scanner.nextLine();

                try {

                    Command cmd = new LoginCommand(userId, password);
                    Request request = new Request("localhost", Server.PORT, cmd.toCommandString(false));
                    Response response = client.request(request);
                    if (response.getStatus() == Response.STATUS_PAGE_NOT_FOUND) {
                        System.out.print("Username or Password is Incorrect!! please try again\n\n");
                        continue;
                    }

                    currentUser = new Users();
                    currentUser.deserialize(response.getBody());

                } catch (IOException | InvalidKeyException e) {
                    e.printStackTrace();
                }
            }

            if (currentUser == null) {
                break;
            }

            while (true) {
                System.out.println("=======================================");
                System.out.println(">>> " + currentUser.getUsername() + " (" + currentUser.getId() + "/" + currentUser.getType() + ")");
                int i = 1;
                for (Command c : Command.list()) {
                    if (c.showInUi(currentUser)) {
                        System.out.println(i + ". " + c.getCommandStringUi());
                        i++;
                    }
                }
                System.out.println("or type 'logout' to logout.");
                System.out.print("Enter Command Number: ");

                String tmpCmd = scanner.nextLine();
                if (tmpCmd.equals("logout")) {
                    currentUser = null;
                    break;
                }

                int commandNumber;
                try {
                    commandNumber = Integer.parseInt(tmpCmd);
                } catch (NumberFormatException e) {
                    System.out.println("Unknown Command, Please select enter command number only!");
                    continue;
                }
                if (commandNumber < 1 || commandNumber > Command.commandInUiCount()) {
                    System.out.println("Unknown Command, Please try again!");
                    continue;
                }

                Command command = null;
                Request request;
                Response response;

                i = 1;
                DatabaseUtility db = DatabaseUtility.connect();
                for (Command c : Command.list()) {
                    if (i == commandNumber) {
                        c.doInputUi(db);
                        command = c;
                        break;
                    }
                    if (c.showInUi(currentUser)) {
                        i++;
                    }
                }

                request = new Request("localhost", Server.PORT, command.toCommandString(false));

                request.setUser(currentUser.getId(), currentUser.getType());

                try {
                    Log.i("Client request with: " + request.getMessage() + " to server " + request.getHost() + ":" + request.getPort());
                    response = client.request(request);
                    Log.i("status: " + response.getStatus());
                    Log.i("body: " + response.getBody());

                    if (response.getStatus() == Response.STATUS_OK) {
                        switch (commandNumber) {
                            case 1:
                                handleListSubject(response);        ///////handle command assessment by subject
                                break;
                            case 2:
                                handleStudentGradeAssessment(response);  //////handle command student grade by subject
                                break;

                            case 3:
                                handleListAssessmentDetail(response); ////handle show assessment table
                                break;

                            case 5:
                                handleAddStudent(response); ////handle show assessment table
                                break;

                            default:
                                System.out.println(response.getBody());
                        }
                    } else {
                        System.out.println("=======================================");
                        System.out.println("Response: Failure!");
                        System.out.println("Error Code: " + response.getStatus());
                        System.out.println(response.getBody());
                        System.out.println("=======================================");
                        System.out.println();
                    }

                } catch (IOException | InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    ////////////////////handle of command ///////////////////////////////////////////////////////////////////
    //////////////command 1///////////////////
    private static void handleListSubject(Response response) {
        List<SubjectAssessment> subjectAssessments = new ArrayList<>();
        System.out.println("=======================================");
        System.out.println("Response: Success! list subject");
        System.out.println("Result:");
        for (String line : response.getBody().split("\n")) {
            if (line.trim().isEmpty()) continue;
            Map<String, Object> data = JsonHelper.flatten(line);
            Log.i("data --> " + data);
            SubjectAssessment s = new SubjectAssessment(
                    (int) data.get("subject_id"),
                    (String) data.get("subject_name"),
                    (String) data.get("assessment_id"),
                    (String) data.get("type"),
                    (String) data.get("topic"),
                    (String) data.get("format"),
                    (String) data.get("due_date")
            );
            subjectAssessments.add(s);

        }


        String[] headers = SubjectAssessment.getHeaders();
        String[][] content = Tablable.of(subjectAssessments, headers.length);

        for (String ss : headers) {
            Log.i(ss);
        }
        for (String[] cc : content) {
            for (String ccc : cc) {
                Log.i(ccc + ", ");
            }
        }

        System.out.println(Table.of(headers, content));
    }

    /////////////////command 2//////////////////
    private static void handleStudentGradeAssessment(Response response) {
        List<GradeAssessment> students = new ArrayList<>();
        System.out.println("=======================================");
        System.out.println("Response: Success!");
        System.out.println("Result:");
        for (String line : response.getBody().split("\n")) {
            if (line.trim().isEmpty()) continue;
            Map<String, Object> data = JsonHelper.flatten(line);
            GradeAssessment a = new GradeAssessment(
                    (int) data.get("student_id"),  //from student class
                    (int) data.get("subject_id"),  //from subject class
                    (String) data.get("assessment_id"), //from assessment class
                    (int) data.get("grade_id")    //from grade class
            );
            students.add(a);
        }
        String[] headers = GradeAssessment.getHeaders();
        String[][] content = Tablable.of(students, headers.length);
        System.out.println(Table.of(headers, content));
    }

    ///////////command 3////////////////////
    private static void handleListAssessmentDetail(Response response) {
        List<Assessment> assessment = new ArrayList<>();
        System.out.println("=======================================");
        System.out.println("Response: Success!");
        System.out.println("Result:");
        for (String line : response.getBody().split("\n")) {
            if (line.trim().isEmpty()) continue;
            Map<String, Object> data = JsonHelper.flatten(line);
            Assessment a = new Assessment(
                    (String) data.get("assessment_id"),
                    (String) data.get("subject"),
                    (String) data.get("type"),
                    (String) data.get("topic"),
                    (String) data.get("format"),
                    (String) data.get("dueDate")
            );
            assessment.add(a);
        }

        String[] headers = Assessment.getHeaders();
        String[][] content = Tablable.of(assessment, headers.length);
        System.out.println(Table.of(headers, content));
    }

    ///////////command 5////////////////////
    private static void handleAddStudent(Response response) {
        List<Assessment> assessment = new ArrayList<>();
        System.out.println("=======================================");
        System.out.println("Response: Success!");
        System.out.println("Result:");
        for (String line : response.getBody().split("\n")) {
            if (line.trim().isEmpty()) continue;
            Map<String, Object> data = JsonHelper.flatten(line);
            Assessment a = new Assessment(
                    (String) data.get("assessment_id"),
                    (String) data.get("subject"),
                    (String) data.get("type"),
                    (String) data.get("topic"),
                    (String) data.get("format"),
                    (String) data.get("dueDate")
            );
            assessment.add(a);
        }

        String[] headers = Assessment.getHeaders();
        String[][] content = Tablable.of(assessment, headers.length);
        System.out.println(Table.of(headers, content));
    }


}
