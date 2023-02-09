package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver implements Runnable {
    private int port;
    private DatagramSocket socket;

    public UDPReceiver(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        socket = new DatagramSocket(port);
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                // process the received message
                System.out.println("Received message: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (socket != null) {
            socket.close();
        }
    }
}

