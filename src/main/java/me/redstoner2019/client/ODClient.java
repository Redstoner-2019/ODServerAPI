package me.redstoner2019.client;

import me.redstoner2019.events.ConnectionFailedEvent;
import me.redstoner2019.events.ConnectionLostEvent;
import me.redstoner2019.events.ConnectionSuccessEvent;
import me.redstoner2019.events.ObjectRecieveEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ODClient {
    private List<ConnectionFailedEvent> failedEvents = new ArrayList<>();
    public List<ConnectionLostEvent> lostEvents = new ArrayList<>();
    private List<ConnectionSuccessEvent> successEvents = new ArrayList<>();
    private List<ObjectRecieveEvent> recieveEvents = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean disconnecting;

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public boolean isDisconnecting() {
        return disconnecting;
    }

    public void connect(String ip, int port){
        try {
            socket = new Socket(ip,port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            for(ConnectionFailedEvent con : failedEvents){
                con.onEvent();
            }
        }
        for(ConnectionSuccessEvent con : successEvents){
            con.onEvent();
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
                    } catch (IOException | ClassNotFoundException e) {
                        if(!disconnecting) for(ConnectionLostEvent con : lostEvents){
                            con.onEvent();
                        }
                        break;
                    }
                }
            }
        });
        t.start();
    }
    public void addConnectionFailedEvent(ConnectionFailedEvent e){
        failedEvents.add(e);
    }
    public void addConnectionLostEvent(ConnectionLostEvent e){
        lostEvents.add(e);
    }
    public void addConnectionSuccessEvent(ConnectionSuccessEvent e){
        successEvents.add(e);
    }
    public void addObjectReiecedEvent(ObjectRecieveEvent e){
        recieveEvents.add(e);
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
}
