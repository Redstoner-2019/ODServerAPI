import me.redstoner2019.events.ClientConnectEvent;
import me.redstoner2019.events.ConnectionLostEvent;
import me.redstoner2019.events.ObjectRecieveEvent;
import me.redstoner2019.server.ODClientHandler;
import me.redstoner2019.server.ODServer;

public class Server extends ODServer {
    public static void main(String[] args) {
        Server server = new Server();
    }
    public Server(){
        setPort(8008);
        addClientConnectEvent(new ClientConnectEvent() {
            @Override
            public void onEvent(ODClientHandler handler) {
                System.out.println("Client connected");
                handler.startListener();
                handler.addConnectionLostEvent(new ConnectionLostEvent() {
                    @Override
                    public void onEvent() {
                        System.out.println("Client disconnected");
                    }
                });
                handler.addObjectRecieveEvent(new ObjectRecieveEvent() {
                    @Override
                    public void onEvent(Object o) {
                        System.out.println(o);
                        handler.sendObject("Pong");
                    }
                });
            }
        });
        start();
    }
}
