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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
                String[] messageSplit = message.split(ServerFrame.splitterString);
                if (messageSplit[0].equals("send-to-global")) {
                    ServerFrame.serverThreadBus.broadCast(this.getClientUsername(), "global-message" + ServerFrame.splitterString + this.getClientUsername() + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    ServerFrame.serverThreadBus.sendMessageToPerson(messageSplit[2], this.getClientUsername() + " (to you): " + messageSplit[1]);
                }
                if (messageSplit[0].equals("request-login")) {
                    boolean loginStatus = DatabaseConnect.verifyLogin(messageSplit[1], messageSplit[2]);
                    if (loginStatus) {
                        if (!ServerFrame.serverThreadBus.isOnline(messageSplit[1])) {
                            write("login-status" + ServerFrame.splitterString + "successful");
                            write("get-clientUsername" + ServerFrame.splitterString + messageSplit[1]);
                            this.clientUsername = messageSplit[1];
                            ServerFrame.serverThreadBus.sendOnlineList();
                            ServerFrame.serverThreadBus.mutilCastSend("global-message" + ServerFrame.splitterString + this.getClientUsername() + " has entered the chat.");
                        } else {
                            write("login-an-online-account" + ServerFrame.splitterString + "true");
                        }
                    } else {
                        write("login-status" + ServerFrame.splitterString + "failed");
                    }
                }
                if (messageSplit[0].equals("request-signup")) {
                    boolean signupStatus = DatabaseConnect.verifySignup(messageSplit[1], messageSplit[2]);
                    if (signupStatus) {
                        write("signup-status" + ServerFrame.splitterString + "successful");

                    } else {
                        write("signup-status" + ServerFrame.splitterString + "failed");

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
                if (messageSplit[0].equals("request-save-files")) {
                    sendAllFilesToClient(messageSplit[1], messageSplit[2], messageSplit[3]);
                }
            }
        } catch (IOException e) {
            isClosed = true;
            ServerFrame.serverThreadBus.remove(clientUsername);
            ServerFrame.logMessage(this.getClientUsername() + " has left the chat.");
            ServerFrame.serverThreadBus.sendOnlineList();
            ServerFrame.serverThreadBus.mutilCastSend("global-message" + ServerFrame.splitterString + this.getClientUsername() + " has left the chat.");
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

    //chuyển đổi một file thành dạng chuỗi fileData
    String getFileData(File file) {
        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            String base64FileData = Base64.getEncoder().encodeToString(fileData);
            return base64FileData;
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    void sendAllFilesToClient(String clientUsername, String pathToSave, String filesToSend) {
        String allFileData = "";
        String allFilenames = "";

        String files[] = filesToSend.split(":::::");

        for (int i = 0; i < files.length; i++) {
            String fileInfo[] = files[i].split(";;;;;");
            String sender = fileInfo[0];
            String fileName = fileInfo[1];

            String filePath = "./src/main/resources/" + sender + "/" + fileName;
            File file = new File(filePath);
            allFileData = allFileData + getFileData(file) + ";:::::;";
            allFilenames = allFilenames + file.getName() + ";:::::;";
        }

        ServerFrame.serverThreadBus.sendFileToPerson(clientUsername, "&:::::&" + allFileData + "&:::::&" + allFilenames + "&:::::&" + pathToSave);
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();

    }
}
