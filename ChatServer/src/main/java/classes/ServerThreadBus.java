/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import frames.ServerFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Danh Del Rey
 */
public class ServerThreadBus {

    private List<ServerThread> listServerThreads;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadBus() {
        listServerThreads = new ArrayList<>();
    }

    public void add(ServerThread serverThread) {
        listServerThreads.add(serverThread);
    }

    public void mutilCastSend(String message) { //like sockets.emit in socket.io
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            try {
                serverThread.write(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void broadCast(String clientUsername, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientUsername().equals(clientUsername)) {
                continue;
            } else {
                try {
                    serverThread.write(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //cập nhật table và hiển thị lên box chat ai đã gửi file gì
    public void updateFileListGlobal(String senderUsername, String fileName, float fileSize, String dateTime, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientUsername().equals(senderUsername)) {
                continue;
            } else {
                try {
                    serverThread.write("update-file-list" + ServerFrame.splitterString + senderUsername + ServerFrame.splitterString + fileName + ServerFrame.splitterString + fileSize + ServerFrame.splitterString + dateTime + ServerFrame.splitterString + message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void updateFileListPerson(String senderUsername, String recipientUsername, String fileName, float fileSize, String dateTime, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientUsername().equals(recipientUsername)) {
                try {
                    serverThread.write("update-file-list" + ServerFrame.splitterString + senderUsername + ServerFrame.splitterString + fileName + ServerFrame.splitterString + fileSize + ServerFrame.splitterString + dateTime + ServerFrame.splitterString + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public int getLength() {
        return listServerThreads.size();
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = ServerFrame.serverThreadBus.getListServerThreads();
        for (ServerThread serverThread : threadbus) {
            res += serverThread.getClientUsername() + "-";
        }
        ServerFrame.serverThreadBus.mutilCastSend("update-online-list" + ServerFrame.splitterString + res);
    }

    public void sendMessageToPerson(String clientUsername, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientUsername().equals(clientUsername)) {
                try {
                    serverThread.write("global-message" + ServerFrame.splitterString + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendFileToPerson(String clientUsername, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientUsername().equals(clientUsername)) {
                try {
                    serverThread.write("get-files" + ServerFrame.splitterString + message);

                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void remove(String clientUsername) {
        for (int i = 0; i < ServerFrame.serverThreadBus.getLength(); i++) {
            if (ServerFrame.serverThreadBus.getListServerThreads().get(i).getClientUsername().equals(clientUsername)) {
                ServerFrame.serverThreadBus.listServerThreads.remove(i);
            }
        }
    }

    public boolean isOnline(String clientUsername) {
        for (int i = 0; i < ServerFrame.serverThreadBus.getLength(); i++) {
            String onlineClientUsername = ServerFrame.serverThreadBus.getListServerThreads().get(i).getClientUsername();
            if (onlineClientUsername != null && onlineClientUsername.equals(clientUsername)) {
                return true;
            }
        }
        return false;
    }
}
