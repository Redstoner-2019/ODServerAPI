package me.redstoner2019.server;

import me.redstoner2019.events.ClientConnectEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ODServer {
    private int port;
    private List<ClientConnectEvent> connectEvents = new ArrayList<>();
    public ODServer(){}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (serverSocket.isBound()){
                Socket socket = serverSocket.accept();
                ODClientHandler clientHandler = new ODClientHandler(socket);
                for(ClientConnectEvent c : connectEvents){
                    c.onEvent(clientHandler);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    public void addClientConnectEvent(ClientConnectEvent clientConnectEvent){
        connectEvents.add(clientConnectEvent);
    }
}
