package Server;

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
    // Implement the methods defined in the Remote interface
    public GroupServerImpl() throws RemoteException {
        subscriptions = new HashMap<>();
        clients = new ArrayList<>();
    }

    @Override
    public boolean join(String ip, int port) throws RemoteException {
        if (clients.size() < MAXCLIENT) {
            ClientInfo client = new ClientInfo(ip, port);
            clients.add(client);
            return true;
        }
        return false;
    }
    @Override
    public boolean leave(String ip, int port) throws RemoteException {
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

    @Override
    public boolean subscribe(String ip, int port, String article) throws RemoteException {
//        ClientInfo client = new ClientInfo(ip, port);
//        String[] fields = article.split(";");
//        if (fields.length < 4) {
//            return false;
//        }
//        String type = fields[0];
//        if (!subscriptions.containsKey(type)) {
//            subscriptions.put(type, new ArrayList<ClientInfo>());
//        }
//        return subscriptions.get(type).add(client);

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

    @Override
    public boolean unsubscribe(String ip, int port, String article) throws RemoteException {
//        ClientInfo client = new ClientInfo(ip, port);
//        String[] fields = article.split(";");
//        if (fields.length < 4) {
//            return false;
//        }
//        String type = fields[0];
//        if (!subscriptions.containsKey(type)) {
//            return false;
//        }
//        return subscriptions.get(type).remove(client);

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
    }
    @Override
    public boolean publish(String article, String ip, int port) throws RemoteException {
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
        }
        return true;
    }

    public boolean ping() {
        return true;
        // Implement the ping method
    }

    public String greeting() {
        return "Hello, CSCI5105-P1 !";
    }
}
