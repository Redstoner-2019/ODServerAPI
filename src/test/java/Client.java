import me.redstoner2019.client.ODClient;
import me.redstoner2019.events.ConnectionLostEvent;
import me.redstoner2019.events.ConnectionSuccessEvent;
import me.redstoner2019.events.ObjectRecieveEvent;

public class Client extends ODClient {
    public static void main(String[] args) {
        Client client = new Client();
    }
    public Client(){
        addConnectionSuccessEvent(new ConnectionSuccessEvent() {
            @Override
            public void onEvent() {
                System.out.println("Connected");
            }
        });
        addConnectionLostEvent(new ConnectionLostEvent() {
            @Override
            public void onEvent() {
                System.out.println("Connection Lost");
            }
        });
        addObjectReiecedEvent(new ObjectRecieveEvent() {
            @Override
            public void onEvent(Object o) {
                System.out.println(o);
            }
        });
        connect("localhost",1234);
        startListener();
        sendObject("Ping");
    }
}
