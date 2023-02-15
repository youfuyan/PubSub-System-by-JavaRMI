package Client;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetAddress;

import Server.GroupServer;

public class Client {
    private GroupServer groupServer;
    static int pingCount = 0;
    private int port;
    private InetAddress address;
    private String currentMessage;
    private int UDPCount = 0;


    public Client() {
        try {
            // Find the registry and get the stub of the group server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            groupServer = (GroupServer) registry.lookup("GroupServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentMessage() {
        return this.currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }

    public int getUDPCount(){
        return this.UDPCount;
    }

    public void setUDPCount(int count){
        this.UDPCount = count;
    }

    public void addoneUDPCount(){
        this.UDPCount ++;
    }

    public boolean join(String ip, int port) {
        boolean join_status = false;
        try {
            join_status = groupServer.join(ip, port);
            if (join_status){
                System.out.println("Join success");
                this.port = port;
                this.address = InetAddress.getByName(ip);
            }
            else{
                System.out.println("Join failed");
            }
            return join_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Join failed");
        return join_status;
    }

    public boolean leave(String ip, int port) {
        boolean leave_status = false;
        try {
            leave_status = groupServer.leave(ip, port);
            if (leave_status) {
                System.out.println("Leave success");
                groupServer.unsubscribeAll(ip, port);
            }
            else{
                System.out.println("Leave failed");
            }
            return leave_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Leave failed");
        return leave_status;
    }

    public boolean subscribe(String ip, int port, String article) {
        boolean subscribe_status = false;
        try {
            subscribe_status = groupServer.subscribe(ip, port, article);
            if(subscribe_status) System.out.println("Subscribe success");
            else System.out.println("Subscribe failed");
            return subscribe_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Subscribe failed");
        return false;
    }

    public boolean unsubscribe(String ip, int port, String article) {
        boolean unsubscribe_status = false;
        try {
            unsubscribe_status = groupServer.unsubscribe(ip, port, article);
            if(unsubscribe_status) System.out.println("Unsubscribe success");
            else System.out.println("Unsubscribe failed");
            return unsubscribe_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Unsubscribe failed");
        return false;
    }

    public boolean unsubscribeAll(String ip, int port) {
        boolean unsubscribe_all_status = false;
        try {
            unsubscribe_all_status = groupServer.unsubscribeAll(ip, port);
            if(unsubscribe_all_status) System.out.println("Unsubscribe All success");
            else System.out.println("Unsubscribe All failed");
            return unsubscribe_all_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Unsubscribe All failed");
        return false;
    }

    public boolean publish(String article, String ip, int port) {
        boolean publish_status = false;
        try {
            publish_status = groupServer.publish(article, ip, port);
            if(publish_status) System.out.println("Publish success");
            else System.out.println("Publish failed");
            return publish_status;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Publish failed");
        return publish_status;
    }

    public void ping(int interval, int pingNum) {
        final Timer timer =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run(){
                boolean pingResult = false;
                try{
                    pingResult = groupServer.ping();
                    System.out.println("Ping scuccess, Ping #seq is " + (pingCount+1));
                } catch (RemoteException e){
                    e.printStackTrace();
                    System.out.println("Ping Failed, Ping #seq is " + (pingCount+1));
                }
                pingCount ++;
                if (pingCount == pingNum){
                    timer.cancel();
                }
            }
        }, 0, interval);
    }

    public void receiveUDP(int port, String ip, Client client){
        try{
            InetAddress address = InetAddress.getByName(ip);
            UDPReceiver receiver = new UDPReceiver(port, address,client);
            receiver.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String greeting() {
        try {
            return groupServer.greeting();
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        System.out.println(client.greeting());
        System.out.println("Client ready");
        client.ping(100, 5);
    }
}
