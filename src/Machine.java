import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Machine extends Remote {
	public String lecture(String donnees) throws RemoteException, FileNotFoundException, IOException;
	public Boolean ecriture(String nom, String donnees) throws RemoteException, FileNotFoundException, IOException;
	public String getNom() throws RemoteException;
}