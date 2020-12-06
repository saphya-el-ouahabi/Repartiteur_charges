import java.rmi.Remote;
import java.rmi.RemoteException;


public interface AiguilleurInterface extends Remote, Machine {
	public boolean addMachine(Machine m) throws RemoteException;
    public boolean removeMachine(Machine m) throws RemoteException;
    public boolean notification(Machine m, int a) throws RemoteException;
}