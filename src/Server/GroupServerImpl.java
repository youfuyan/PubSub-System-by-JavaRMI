package Server;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow.Subscriber;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {
    private class ClientInfo {
        private String IP;
        private int Port;
        private ArrayList<String> SubscribedArticles;

        public ClientInfo(String IP, int Port) {
            this.IP = IP;
            this.Port = Port;
        }

        public String getIP() {
            return IP;
        }

        public int getPort() {
            return Port;
        }

        public ArrayList<String> getSubscribedArticles(){
            return SubscribedArticles;
        }

        public void addSubscribedArticle(String article){
            if (this.SubscribedArticles == null){
                this.SubscribedArticles = new ArrayList<>();
            }
            this.SubscribedArticles.add(article);
        }

        public void removeSubscribedArticle(String article){
            this.SubscribedArticles.remove(article);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof ClientInfo)) return false;
            ClientInfo c = (ClientInfo) o;
            return IP.equals(c.IP) && Port == c.Port;
        }
    }
    private class Publication{
        private String Article;
        private String publisherIP;
        private int publisherPort;
        private String Type;
        private String Originator;
        private String Org;
        private String Contents;

        public Publication(String article, String publisherip, int publisherport){
            this.Article = article;
            this.publisherIP =publisherip;
            this.publisherPort = publisherport;
            String[] parts = article.split(";");
            this.Type = parts[0];
            this.Originator = parts[1];
            this.Org = parts[2];
            this.Contents = parts[3];
        }

        public String getArticle() {
            return Article;
        }
        public String getType() {
            return Type;
        }
        public String getOriginator() {
            return Originator;
        }
        public String getOrg() {
            return Org;
        }
        public String getContents() {
            return Contents;
        }
    }

    // private HashMap<String, ArrayList<ClientInfo>> subscriptions1;
    // private HashMap<String, ArrayList<ClientInfo>> subscriptions2;
    // private HashMap<String, ArrayList<ClientInfo>> subscriptions3;
    private ArrayList<ClientInfo> clients;
    private ArrayList<Publication> publications;
    private static final int MAXCLIENT = 10;

    private final String[] types = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
    // Implement the methods defined in the Remote interface
    public GroupServerImpl() throws RemoteException {
        // subscriptions1 = new HashMap<>();
        // subscriptions2 = new HashMap<>();
        // subscriptions3 = new HashMap<>();
        clients = new ArrayList<>();
        publications = new ArrayList<>();
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
                for (int i = 0; i < clients.size(); i++) {
                    System.out.println(clients.get(i));
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

    public synchronized boolean leave(String ip, int port) throws RemoteException {
        try {
            ClientInfo client = new ClientInfo(ip, port);
            if (clients.contains(client)) {
                clients.remove(client);
                // for (List<ClientInfo> subscribers : subscriptions.values()) {
                //     subscribers.remove(client);
                // }
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
            // if (parts.length != 4) {
            //     return false;
            // }
            String type = parts.length > 0 ? parts[0] : "";
            if((!Arrays.asList(types).contains(type)) && type != "") return false;
            String originator = parts.length > 1 ? parts[1] : "";
            String org = parts.length > 2 ? parts[2] : "";
            if((type.equals("")) && (originator.equals("")) && (org.equals(""))) return false;
            if (clients.contains(client)) {
                int index = clients.indexOf(client);
                clients.get(index).addSubscribedArticle(type+";"+originator+";"+org);
            }
            else return false;
            /*List<ClientInfo> subscribers1 = subscriptions1.get(type);
            List<ClientInfo> subscribers2 = subscriptions2.get(originator);
            List<ClientInfo> subscribers3 = subscriptions3.get(org);
            if(type != ""){
                if (subscribers1 == null) {
                    subscribers1 = new ArrayList<>();
                    subscriptions1.put(type, (ArrayList<ClientInfo>) subscribers1);
                }
                subscribers1.add(client);
            }
            if(originator != ""){
                if (subscribers2 == null) {
                    subscribers2 = new ArrayList<>();
                    subscriptions2.put(originator, (ArrayList<ClientInfo>) subscribers2);
                }
                subscribers2.add(client);
            }
            if(org != ""){
                if (subscribers3 == null) {
                    subscribers3 = new ArrayList<>();
                    subscriptions3.put(org, (ArrayList<ClientInfo>) subscribers3);
                }
                subscribers3.add(client);
            }*/
            for (Publication publication : publications){
                String pubtype = publication.getType();
                String puboriginator = publication.getOriginator();
                String puborg = publication.getOrg();
                if(((type.equals(pubtype))|| type.equals("")) && ((originator.equals(puboriginator))|| originator.equals("")) && ((org.equals(puborg))|| org.equals(""))){
                    // send article containing contents to this client by UDP
                    SendUDP(publication.getArticle(), port, ip);
                }
            }
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
            // if (parts.length != 4) {
            //     return false;
            // }
            String type = parts.length > 0 ? parts[0] : "";
            if((!Arrays.asList(types).contains(type)) && type != "") return false;
            String originator = parts.length > 1 ? parts[1] : "";
            String org = parts.length > 2 ? parts[2] : "";
            if((type.equals("")) && (originator.equals("")) && (org.equals(""))) return false;

            if (clients.contains(client)) {
                int index = clients.indexOf(client);
                if(clients.get(index).getSubscribedArticles().contains(type+";"+originator+";"+org)){
                    clients.get(index).removeSubscribedArticle(type+";"+originator+";"+org);
                    return true;
                }
                else return false;
            }
            else return false;
            /* 
            List<ClientInfo> subscribers1 = subscriptions1.get(type);
            List<ClientInfo> subscribers2 = subscriptions2.get(originator);
            List<ClientInfo> subscribers3 = subscriptions3.get(org);
            if (subscribers1 == null) return false;
            if (subscribers2 == null) return false;
            if (subscribers3 == null) return false;
            boolean remove_satue = false;
            if (subscribers1.contains(client)) {
                subscribers1.remove(client);
                remove_satue = true;
            }
            if (subscribers2.contains(client)) {
                subscribers2.remove(client);
                remove_satue = true;
            }
            if (subscribers3.contains(client)) {
                subscribers3.remove(client);
                remove_satue = true;
            }
            return remove_satue;
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean publish(String article, String ip, int port) throws RemoteException {
        try {
            boolean publish_status = false;
            ClientInfo client = new ClientInfo(ip, port);
            if (!clients.contains(client)) {
                return false;
            }
            String[] parts = article.split(";");
            if (parts.length != 4) {
                return false;
            }
            String type = parts[0];
            String originator = parts[1];
            String org = parts[2];
            String contents = parts[3];
            
            
            if ((!(type.equals("") && originator.equals("") && org.equals(""))) && (!contents.equals(""))){
                if((Arrays.asList(types).contains(type)) || type.equals("") ){
                Publication publication = new Publication(article, ip, port);
                publications.add(publication);
                publish_status = true;
                }
            }
            else publish_status = false;
            String[] toSendArticles = new String[]{type+";"+originator+";"+org,type+";"+originator+";",";"+originator+";"+org,
                type+";"+";"+org,type+";"+";",";"+originator+";",";"+";"+org};
            for(ClientInfo existclient : clients){
                boolean isSendArticle = false;
                for(String toSendArticle : toSendArticles){
                    if(existclient.getSubscribedArticles() == null) isSendArticle = false;
                    else if(existclient.getSubscribedArticles().contains(toSendArticle)) isSendArticle = true;
                }
                if (isSendArticle){
                    SendUDP(article,existclient.getPort(),existclient.getIP());
                }
            }
            return publish_status;
            /* 
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
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean ping() {
        return true;
    }

    public void SendUDP(String article,int port, String address)throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = article.getBytes();
        InetAddress clientAddress = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress, port);
        socket.send(packet);
        socket.close();
    }

    public synchronized String greeting() {
        return "Hello, CSCI5105-P1 !";
    }
}
