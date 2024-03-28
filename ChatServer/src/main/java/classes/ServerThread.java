/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import frames.ServerFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
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

                if (messageSplit[0].equals("send-file-to-global")) {
                    saveFileToServer(messageSplit[1], messageSplit[2], messageSplit[3], messageSplit[5]);
                    ServerFrame.serverThreadBus.updateFileListGlobal(messageSplit[2], messageSplit[3], Float.parseFloat(messageSplit[4]), messageSplit[5], this.clientUsername + " has sent a file: " + messageSplit[3]);
                }
                if (messageSplit[0].equals("send-file-to-person")) {
                    saveFileToServer(messageSplit[1], messageSplit[2], messageSplit[3], messageSplit[5]);
                    ServerFrame.serverThreadBus.updateFileListPerson(messageSplit[2], messageSplit[6], messageSplit[3], Float.parseFloat(messageSplit[4]), messageSplit[5], messageSplit[2] + " has sent a file: " + messageSplit[3] + " (to you)");
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

    //lưu file vào thư mục resources của server, khi nào client yêu cầu tải thì server sẽ từ đó mà gửi file
    void saveFileToServer(String base64FileData, String clientUsername, String fileName, String dateTime) {
        byte[] fileData = Base64.getDecoder().decode(base64FileData);

        //Tạo đường dẫn để lưu file vào server
        String pathToSave = "./src/main/resources/" + clientUsername + "/";
        Path filePath = Paths.get(pathToSave, fileName);

        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        Path directoryPath = Paths.get("./src/main/resources/", clientUsername);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            // Lưu file vào đường dẫn đã tạo
            Files.write(filePath, fileData);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendFileToClient(String clientUsername, String sender, String fileName) {
        String directoryPath = "./src/main/resources/" + sender + "/";
        // Tạo một đối tượng File đại diện cho thư mục
        File directory = new File(directoryPath);

        // Kiểm tra xem nó có tồn tại và có phải là thư mục không
        if (directory.exists() && directory.isDirectory()) {
            // Lấy danh sách các tệp trong thư mục
            File[] files = directory.listFiles();

            // Lặp qua từng tệp
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(fileName)) {
                        //gui filedata64 cho client
                    }
                }
            }
        } else {
            System.out.println("Đường dẫn không tồn tại hoặc không phải là một thư mục.");
        }

    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();

    }
}
