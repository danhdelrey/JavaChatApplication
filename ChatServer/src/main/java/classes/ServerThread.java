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

/**
 *
 * @author Danh Del Rey
 */
public class ServerThread implements Runnable {

    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        ServerFrame.logMessage("Server thread number " + clientNumber + " Started");
        isClosed = false;
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            ServerFrame.logMessage("Khời động luông mới thành công, ID là: " + clientNumber);

            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("send-to-global")) {
                    ServerFrame.serverThreadBus.broadCast(this.getClientNumber(), "global-message" + "," + "Client " + messageSplit[2] + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    ServerFrame.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]), "Client " + messageSplit[2] + " (tới bạn): " + messageSplit[1]);
                }
                if (messageSplit[0].equals("request_login")) {
                    boolean loginStatus = DatabaseConnect.verifyLogin(messageSplit[1], messageSplit[2]);
                    if (loginStatus) {
                        write("login_status" + "," + "successful");
                        write("get-id" + "," + this.clientNumber);
                        ServerFrame.serverThreadBus.sendOnlineList();
                        ServerFrame.serverThreadBus.mutilCastSend("global-message" + "," + "---Client " + this.clientNumber + " đã đăng nhập---");
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
            }
        } catch (IOException e) {
            isClosed = true;
            ServerFrame.serverThreadBus.remove(clientNumber);
            ServerFrame.logMessage(this.clientNumber + " đã thoát");
            ServerFrame.serverThreadBus.sendOnlineList();
            ServerFrame.serverThreadBus.mutilCastSend("global-message" + "," + "---Client " + this.clientNumber + " đã thoát---");
        }
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();

    }
}
