package Client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

import Server.GroupServer;
import Client.UDPReceiver;

public class Client {
    private GroupServer groupServer;

    public Client() {
        try {
            // Find the registry and get the stub of the group server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            groupServer = (GroupServer) registry.lookup("GroupServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public boolean join(String ip, int port) {
        try {
            groupServer.join(ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void leave(String ip, int port) {
        try {
            groupServer.leave(ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean subscribe(String ip, int port, String article) {
        try {
            groupServer.subscribe(ip, port, article);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unsubscribe(String ip, int port, String article) {
        try {
            groupServer.unsubscribe(ip, port, article);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean publish(String article, String ip, int port) {
        try {
            groupServer.publish(article, ip, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void ping(int interval, int pingNum) {
        final Timer timer =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run(){
                int pingCount = 0;
                boolean pingResult = false;
                try{
                    pingResult = groupServer.ping();
                    System.out.println("Ping scuccess, Ping #seq is" + (pingCount+1));
                } catch (RemoteException e){
                    e.printStackTrace();
                    System.out.println("Ping Failed, Ping #seq is" + (pingCount+1));
                }
                pingCount ++;
                if (pingCount == pingNum){
                    timer.cancel();
                }
            }
        }, 0, interval);
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
        System.out.println("Client ready.");
        //client.ping(1000, 10);

        UDPReceiver udpReceiver = new UDPReceiver(1090);
        try {
            udpReceiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
