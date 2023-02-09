package Server;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {
    private class ClientInfo {
        private String IP;
        private int Port;

        public ClientInfo(String IP, int Port) {
            this.IP = IP;
            this.Port = Port;
        }
    }
    private HashMap<String, ArrayList<ClientInfo>> subscriptions;
    private ArrayList<ClientInfo> clients;
    private static final int MAXCLIENT = 10;

    private final String[] types = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
    // Implement the methods defined in the Remote interface
    public GroupServerImpl() throws RemoteException {
        subscriptions = new HashMap<>();
        clients = new ArrayList<>();
    }
    /*
    proper synchronization of access to shared resources is important in RMI to avoid any race conditions.
    The synchronized keyword can be used to lock shared objects while they are being accessed by multiple threads.
    */
    public synchronized boolean join(String ip, int port) throws RemoteException {
        try {
            if (clients.size() < MAXCLIENT) {
                ClientInfo client = new ClientInfo(ip, port);
                clients.add(client);
                return true;
        }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean leave(String ip, int port) throws RemoteException {
        try {
            ClientInfo client = new ClientInfo(ip, port);
            if (clients.contains(client)) {
                clients.remove(client);
                for (List<ClientInfo> subscribers : subscriptions.values()) {
                    subscribers.remove(client);
                }
                return true;
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean subscribe(String ip, int port, String article) throws RemoteException {
        try {
            ClientInfo client = new ClientInfo(ip, port);
            String[] parts = article.split(";");
            if (parts.length != 4) {
                return false;
            }
            String type = parts[0];
            List<ClientInfo> subscribers = subscriptions.get(type);
            if (subscribers == null) {
                subscribers = new ArrayList<>();
                subscriptions.put(type, (ArrayList<ClientInfo>) subscribers);
            }
            subscribers.add(client);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public synchronized boolean unsubscribe(String ip, int port, String article) throws RemoteException {
        try {
            ClientInfo client = new ClientInfo(ip, port);
            String[] parts = article.split(";");
            if (parts.length != 4) {
                return false;
            }
            String type = parts[0];
            List<ClientInfo> subscribers = subscriptions.get(type);
            if (subscribers == null) {
                return false;
            }
            subscribers.remove(client);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean publish(String article, String ip, int port) throws RemoteException {
        try {
            ClientInfo client = new ClientInfo(ip, port);
            if (!clients.contains(client)) {
                return false;
            }
            String[] parts = article.split(";");
            if (parts.length != 4) {
                return false;
            }
            String type = parts[0];
            List<ClientInfo> subscribers = subscriptions.get(type);
            if (subscribers == null) {
                return true;
            }
            for (ClientInfo subscriber : subscribers) {
                // send article to subscriber via UDP
                try {
                    // create a DatagramSocket to send the article
                    DatagramSocket socket = new DatagramSocket();
                    // convert article to a byte array
                    byte[] buffer = article.getBytes();
                    // create a DatagramPacket to send to the subscriber
                    InetAddress address = InetAddress.getByName(subscriber.IP);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, subscriber.Port);
                    // send the packet
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean ping() {
        return true;
    }

    public synchronized String greeting() {
        return "Hello, CSCI5105-P1 !";
    }
}
