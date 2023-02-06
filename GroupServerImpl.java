import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {
    // Implement the methods defined in the Remote interface
    public GroupServerImpl() throws RemoteException {
        // super();
    }

    public void join(String IP, int Port) throws RemoteException {
        // Implement the join method
    }

    public void leave(String IP, int Port) throws RemoteException {
        // Implement the leave method
    }

    public void subscribe(String IP, int Port, String Article) throws RemoteException {
        // Implement the subscribe method
    }

    public void unsubscribe(String IP, int Port, String Article) throws RemoteException {
        // Implement the unsubscribe method
    }

    public void publish(String Article, String IP, int Port) throws RemoteException {
        // Implement the publish method
    }

    public boolean ping() {
        return false;
        // Implement the ping method
    }

    public String greeting() {
        return "Hello, CSCI5105-P1 !";
    }
}
