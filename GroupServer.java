
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GroupServer extends Remote {
    // Define the methods that the client can call on the server
    public void join(String IP, int Port) throws RemoteException;

    public void leave(String IP, int Port) throws RemoteException;

    public void subscribe(String IP, int Port, String Article) throws RemoteException;

    public void unsubscribe(String IP, int Port, String Article) throws RemoteException;

    public void publish(String Article, String IP, int Port) throws RemoteException;

    public boolean ping() throws RemoteException;

    // public void greeting() throws RemoteException;
    public String greeting() throws RemoteException;
}
