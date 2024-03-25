/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import frames.ServerFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import javax.swing.JOptionPane;

/**
 *
 * @author Danh Del Rey
 */
public class ServerThread implements Runnable {

    private Socket socketOfServer;
    private String clientUsername;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public ServerThread(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
        isClosed = false;
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("send-to-global")) {
                    ServerFrame.serverThreadBus.broadCast(this.getClientUsername(), "global-message" + "," + this.getClientUsername() + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    ServerFrame.serverThreadBus.sendMessageToPerson(messageSplit[2], this.getClientUsername() + " (to you): " + messageSplit[1]);
                }
                if (messageSplit[0].equals("request_login")) {
                    boolean loginStatus = DatabaseConnect.verifyLogin(messageSplit[1], messageSplit[2]);
                    if (loginStatus) {
                        if (!ServerFrame.serverThreadBus.isOnline(messageSplit[1])) {
                            write("login_status" + "," + "successful");
                            write("get-clientUsername" + "," + messageSplit[1]);
                            this.clientUsername = messageSplit[1];
                            ServerFrame.serverThreadBus.sendOnlineList();
                            ServerFrame.serverThreadBus.mutilCastSend("global-message" + "," + this.getClientUsername() + " has entered the chat.");
                        } else {
                            write("login_an_online_account" + "," + "true");
                        }
                    } else {
                        write("login_status" + "," + "failed");
                    }
                }
                if (messageSplit[0].equals("request_signup")) {
                    boolean signupStatus = DatabaseConnect.verifySignup(messageSplit[1], messageSplit[2]);
                    if (signupStatus) {
                        write("signup_status" + "," + "successful");
                        System.out.println("dang ky thanh cong");
                    } else {
                        write("signup_status" + "," + "failed");
                        System.out.println("dang ky that bai");
                    }
                }

//                if (messageSplit[0].equals("send-file")) {
//                    String base64FileData = messageSplit[1];
//                    String fileName = messageSplit[2];
//                    String senderUsername = messageSplit[3];
//                    float fileSize = Float.parseFloat(messageSplit[4]);
//                    String dateTime = messageSplit[5];
//
//                    byte[] fileData = Base64.getDecoder().decode(base64FileData);
//
//                    //Tạo đường dẫn để lưu file vào server
//                    String pathToSave = "./src/main/resources/";
//                    Path filePath = Paths.get(pathToSave, fileName);
//
//                    // Lưu file vào đường dẫn đã tạo
//                    Files.write(filePath, fileData);
//
//                    // Gửi thông báo cho các client khác (hoặc thực hiện các xử lý khác)
//                    ServerFrame.serverThreadBus.broadCast(senderUsername, "global-message" + "," + senderUsername + " đã gửi file: " + fileName);
//                }
                if (messageSplit[0].equals("send-file-to-global")) {

                }
                if (messageSplit[0].equals("send-file-to-person")) {

                }
            }
        } catch (IOException e) {
            isClosed = true;
            ServerFrame.serverThreadBus.remove(clientUsername);
            ServerFrame.logMessage(this.getClientUsername() + " has left the chat.");
            ServerFrame.serverThreadBus.sendOnlineList();
            ServerFrame.serverThreadBus.mutilCastSend("global-message" + "," + this.getClientUsername() + " has left the chat.");
        }
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();

    }
}
