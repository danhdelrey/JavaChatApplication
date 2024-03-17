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

    public void broadCast(int id, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
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

    public int getLength() {
        return listServerThreads.size();
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = ServerFrame.serverThreadBus.getListServerThreads();
        for (ServerThread serverThread : threadbus) {
            res += serverThread.getClientNumber() + "-";
        }
        ServerFrame.serverThreadBus.mutilCastSend("update-online-list" + "," + res);
    }

    public void sendMessageToPersion(int id, String message) {
        for (ServerThread serverThread : ServerFrame.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write("global-message" + "," + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void remove(int id) {
        for (int i = 0; i < ServerFrame.serverThreadBus.getLength(); i++) {
            if (ServerFrame.serverThreadBus.getListServerThreads().get(i).getClientNumber() == id) {
                ServerFrame.serverThreadBus.listServerThreads.remove(i);
            }
        }
    }
}
