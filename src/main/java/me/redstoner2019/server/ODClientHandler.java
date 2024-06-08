package me.redstoner2019.server;

import me.redstoner2019.events.ConnectionLostEvent;
import me.redstoner2019.events.ObjectRecieveEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ODClientHandler {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private List<ObjectRecieveEvent> recieveEvents = new ArrayList<>();
    private List<ConnectionLostEvent> connectionLostEvents = new ArrayList<>();
    public HashMap<String, Object> localData = new HashMap<>();
    private boolean disconnecting;
    public ODClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            if(!disconnecting) for(ConnectionLostEvent con : connectionLostEvents){
                con.onEvent();
            }
        }
    }
    public void startListener(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!socket.isClosed()){
                    try {
                        Object o = ois.readObject();
                        for(ObjectRecieveEvent e : recieveEvents){
                            e.onEvent(o);
                        }
                    } catch (Exception e) {
                        if(!disconnecting) for(ConnectionLostEvent con : connectionLostEvents){
                            con.onEvent();
                        }
                        break;
                    }
                }
            }
        });
        t.start();
    }
    public void addObjectRecieveEvent(ObjectRecieveEvent e){
        recieveEvents.add(e);
    }
    public void addConnectionLostEvent(ConnectionLostEvent e){
        connectionLostEvents.add(e);
    }
    public void disconnect(){
        try {
            disconnecting = true;
            socket.close();
            oos.close();
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendObject(Object o){
        try {
            oos.writeObject(o);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }
}
