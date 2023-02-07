package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Server.GroupServer;

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

    public boolean ping() {
        try {
            return groupServer.ping();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
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
        System.out.println("Client ready.");
    }
}
