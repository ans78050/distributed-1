package client;

import common.JsonHelper;
import common.Log;
import common.Tablable;
import common.command.*;
import common.display.Table;
import common.model.*;
import protocol.Request;
import protocol.Response;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client {

    public Response request(Request request) throws IOException {
        Socket socket = createSocket(request);                     //Socket Create
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF(request.getMessage());
        String res = input.readUTF();
        socket.close();

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

    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        Users currentUser = null;

        while (currentUser == null) {
            System.out.println("System need Login");
            System.out.print("username: ");
            String username = scanner.nextLine();
            System.out.print("password: ");
            String password = scanner.nextLine();

            try {

                Command cmd = new LoginCommand(username, password);
                Request request = new Request("localhost", Server.PORT, cmd.toCommandString(false));
                Response response = client.request(request);
                if (response.getStatus() == Response.STATUS_PAGE_NOT_FOUND) {
                    System.out.print("Username or Password is Incorrect!! please try again\n\n");
                    continue;
                }

                currentUser = new Users();
                currentUser.deserialize(response.getBody());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            System.out.println("=======================================");
            System.out.println(">>> " + currentUser.getUsername() + " (" + currentUser.getId() + "/" + currentUser.getType() + ")");
            System.out.println("1. List Of Assessment For Chosen Subject");
            System.out.println("2. Grade Of Assessment For Student");
            System.out.println("3. Set Grade For A Chosen Student And Subject");
            System.out.println("4. List All Assessment Detail");
            System.out.println("or type 'logout' to logout.");
            System.out.print("Enter Command Number: ");

            String s = scanner.nextLine();
            if (s.equals("logout")) {
                break;
            }
            int commandNumber;
            try {
                commandNumber = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Unknown Command, Please select enter command number only!");
                continue;
            }
            if (commandNumber < 0 || commandNumber > 4) {
                System.out.println("Unknown Command, Please try again!");
                continue;
            }

            Command command = null;
            Request request;
            Response response;
            ////////////// command 1 List Of Assessment For Chosen Subject /////////////////////////////////
            if (commandNumber == 1) {
                ListSubjectAssessment s = new ListSubjectAssessment();
                System.out.println("=======================================");
                System.out.println("1. English");
                System.out.println("2. Mathematics B");
                System.out.println("3. Biology");
                System.out.println("4. Business and Communication Technologies");
                System.out.println("5. Religion and Ethics");
                System.out.println("=======================================");
                System.out.print("Enter Subject Id: ");
                s.setSubjectId(scanner.nextInt());
                command = s;
            } else
///////// command 2 Grade Of Assessment For A Student/////////////////////////////////////////////
                if (commandNumber == 2) {
                    GetStudentGradeCommand c = new GetStudentGradeCommand();
                    System.out.println("=======================================");
                    System.out.println("1. John Clarke");
                    System.out.println("2. Peter White");
                    System.out.println("3. Lily Li");
                    System.out.println("4. Lisa Soon");
                    System.out.println("5. Tom Dixon");
                    System.out.println("=======================================");
                    System.out.print("Enter Student Id: ");
                    c.setStudentId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("=======================================");
                    System.out.println("1. English");
                    System.out.println("2. Mathematics B");
                    System.out.println("3. Biology");
                    System.out.println("4. Business and Communication Technologies");
                    System.out.println("5. Religion and Ethics");
                    System.out.println("=======================================");
                    System.out.print("Enter Subject Id: ");
                    c.setSubjectId(Integer.parseInt(scanner.nextLine()));
                    command = c;
                } else
//////////// command 3 Set Grade For A Chosen Student And Subject/////////////////////////////////
                    if (commandNumber == 3) {
                        SetGradeCommand c = new SetGradeCommand();
                        System.out.println("=======================================");
                        System.out.println("1. John Clarke");
                        System.out.println("2. Peter White");
                        System.out.println("3. Lily Li");
                        System.out.println("4. Lisa Soon");
                        System.out.println("5. Tom Dixon");
                        System.out.println("=======================================");
                        System.out.print("Enter Student Id: ");
                        c.setStudentId(scanner.nextInt());
                        scanner.nextLine();
                        System.out.println("=======================================");
                        System.out.println("1. English");
                        System.out.println("2. Mathematics B");
                        System.out.println("3. Biology");
                        System.out.println("4. Business and Communication Technologies");
                        System.out.println("5. Religion and Ethics");
                        System.out.println("=======================================");
                        System.out.print("Enter Subject Id: ");
                        c.setSubjectId(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Enter Assessment Id(eg 11.1): ");
                        c.setAssessmentId(scanner.next());
                        scanner.nextLine();
                        System.out.println("=======================================");
                        System.out.println("1. Very high");
                        System.out.println("2. High");
                        System.out.println("3. Sound");
                        System.out.println("4. Developing");
                        System.out.println("5. Basic understanding");
                        System.out.println("=======================================");
                        System.out.print("Enter Grade Id: ");
                        c.setGradeId(scanner.nextInt());
                        command = c;
                    } else
/////////////command 4 List All Assessment Detail/////////////////////////////////////////////////
                        if (commandNumber == 4) {
                            command = new ListAssessmentDetailCommand();
                        } else {

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

                        case 4:
                            handleListAssessmentDetail(response); ////handle show assessment table
                            break;
                    }
                } else {
                    System.out.println("=======================================");
                    System.out.println("Response: Failure!");
                    System.out.println("Error Code: " + response.getStatus());
                    System.out.println(response.getBody());
                    System.out.println("=======================================");
                    System.out.println();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    ////////////////////handle of command ///////////////////////////////////////////////////////////////////
    //////////////command 1///////////////////
    private static void handleListSubject(Response response) {
        List<SubjectAssessment> subjects = new ArrayList<>();
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
            subjects.add(s);

        }


        String[] headers = Subject.getHeaders();
        String[][] content = Tablable.of(subjects, headers.length);

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

    ///////////command 4////////////////////
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


}
