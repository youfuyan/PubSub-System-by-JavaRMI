package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    public Client() {
        try {
            // Find the registry and get the stub of the group server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            groupServer = (GroupServer) registry.lookup("GroupServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public class UDPReceiver extends Thread{
        private int port;
        private InetAddress address;

        public UDPReceiver (int port,InetAddress address){
            this.port = port;
            this.address = address;
        }

        @Override
        public void run (){
            try{
                DatagramSocket socket =  new DatagramSocket(port);
                while(true){
                    byte [] buffer = new byte[120];
                    DatagramPacket packet  = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    if (packet.getAddress().equals(address)){
                        String message = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("Received article: " + message);
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    public boolean join(String ip, int port) {
        boolean join_status = false;
        try {
            join_status = groupServer.join(ip, port);
            if (join_status){
                System.out.println("Join success");
            }
            else{
                System.out.println("Join failed");
            }
            return join_status;
        } catch (RemoteException e) {
            e.printStackTrace();
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

    public void receiveUDP(int port, String ip){
        try{
            InetAddress address = InetAddress.getByName(ip);
            UDPReceiver receiver = new UDPReceiver(port,address);
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
        System.out.println("Client ready.");
        client.ping(100, 5);

        // UDPReceiver udpReceiver = new UDPReceiver(1099);
        // try {
        //     udpReceiver.start();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        client.receiveUDP(1098,"127.0.0.1");
        //join
        boolean join_status = client.join("127.0.0.1", 1099);
        System.out.println("Join status is " + join_status);
        //boolean leave_status = client.leave("127.0.0.1", 1098);
        //System.out.println("Leave status is " + leave_status);
        boolean join_status2 = client.join("127.0.0.1", 1098);
        client.subscribe("127.0.0.1", 1098, "Sports;;;");
        //client.unsubscribe("127.0.0.1", 1098, "Sports;;;");
        boolean publish_status = client.publish("Sports;UMN;;contents","127.0.0.1", 1099);
        System.out.println("publish status is " + publish_status);
        


    }
}
