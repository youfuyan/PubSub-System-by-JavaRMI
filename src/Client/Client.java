package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetAddress;

import Server.GroupServer;
import Client.UDPReceiver;

public class Client {
    private GroupServer groupServer;
    static int pingCount = 0;

    private int port;
    private InetAddress address;
    private String currentMessage;


    public Client() {
        try {
            // Find the registry and get the stub of the group server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            groupServer = (GroupServer) registry.lookup("GroupServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

//    public class UDPReceiver extends Thread{
//        private int port;
//        private InetAddress address;
//
//        private String message;
//
//        public UDPReceiver (int port,InetAddress address){
//            this.port = port;
//            this.address = address;
//        }
//
//        @Override
//        public void run (){
//            try{
//                DatagramSocket socket =  new DatagramSocket(port);
//                while(true){
//                    byte [] buffer = new byte[120];
//                    DatagramPacket packet  = new DatagramPacket(buffer, buffer.length);
//                    socket.receive(packet);
//                    if (packet.getAddress().equals(address)){
//                        String message = new String(packet.getData(), 0, packet.getLength());
//                        System.out.println("Received article: " + message);
//                    }
//                }
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//
//        }
//
//    }


    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
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

        // UDPReceiver udpReceiver = new UDPReceiver(1099);
        // try {
        //     udpReceiver.start();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        //join
        boolean join_status = client.join("127.0.0.1", 1099);
        System.out.println("Join status is " + join_status);
        //boolean leave_status = client.leave("127.0.0.1", 1098);
        //System.out.println("Leave status is " + leave_status);
        boolean join_status2 = client.join("127.0.0.1", 1098);
        client.subscribe("127.0.0.1", 1098, "Sports;;;");
        //client.unsubscribe("127.0.0.1", 1098, "Sports;;;");
       client.receiveUDP(1098,"127.0.0.1",client);
        boolean publish_status = client.publish("Sports;UMN;;contents","127.0.0.1", 1099);
        //should not receive Eneterainment
        boolean publish_status2 = client.publish("Entertainment;UMN;;contents2","127.0.0.1", 1098);
        System.out.println("publish status is " + publish_status);
        System.out.println("publish status2 is " + publish_status2);


        // test for unsubscribe all
        String[] types = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};

        client.subscribe("127.0.0.1", 1098, types[0]);
        client.subscribe("127.0.0.1", 1098, types[1]);
        client.subscribe("127.0.0.1", 1098, types[2]);
        client.subscribe("127.0.0.1", 1098, types[3]);
        client.subscribe("127.0.0.1", 1098, types[4]);
        client.subscribe("127.0.0.1", 1098, types[5]);
        client.subscribe("127.0.0.1", 1098, types[6]);
        client.subscribe("127.0.0.1", 1098, types[7]);
        boolean publish_status_0 = client.publish(types[0] + ";UMN;;contents0","127.0.0.1", 1099);
        boolean publish_status_1 = client.publish(types[1] + ";UMN;;contents1","127.0.0.1", 1099);
        boolean publish_status_2 = client.publish(types[2] + ";UMN;;contents2","127.0.0.1", 1099);
        boolean publish_status_3 = client.publish(types[3] + ";UMN;;contents3","127.0.0.1", 1099);
        boolean publish_status_4 = client.publish(types[4] + ";UMN;;contents4","127.0.0.1", 1099);
        boolean publish_status_5 = client.publish(types[5] + ";UMN;;contents5","127.0.0.1", 1099);
        boolean publish_status_6 = client.publish(types[6] + ";UMN;;contents6","127.0.0.1", 1099);
        boolean publish_status_7 = client.publish(types[7] + ";UMN;;contents7","127.0.0.1", 1099);
        client.unsubscribeAll("127.0.0.1", 1098);


    }
}
