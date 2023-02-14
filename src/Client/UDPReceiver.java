package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPReceiver implements Runnable {
    private int port;
    private InetAddress address;
    private DatagramSocket socket;

    private String currentMessage;

    private boolean running;

    public String getCurrentMessage() {
        return currentMessage;
    }

    private void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }
    public UDPReceiver (int port, InetAddress address){
        this.port = port;
        this.address = address;
        this.currentMessage = "No message received yet.";
    }
    public void start() throws IOException {
        socket = new DatagramSocket(port);
        new Thread(this).start();
    }

    public void run() {
        running = true;
        while (running == true) {
            try {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                // process the received message
                setCurrentMessage(message);
                System.out.println("Client " + port + " received message: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        socket.close();
    }
}

