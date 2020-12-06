import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;

public class Client {

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
			//Le (Machine) permet de transformer l'objet Remote en objet Machine
			Machine a1 = (Machine) registry.lookup("Aiguilleur");
			System.out.println("L'ecriture s'est bien faite : " + a1.ecriture(args[0],args[1]+"\n"));
			System.out.println("Contenu du fichier : \n" + a1.lecture(args[0])+ "lit : ");
		} catch (NotBoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
