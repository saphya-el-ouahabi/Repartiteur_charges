import java.io.*;
import java.net.*; //pour avoir les Sockets
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

public class Serveur implements Machine {
	private String nom;
	private static Object sync = new Object();
	private static Hashtable<String, Object> dico = new Hashtable<String,Object>();
	
	public Serveur(String nom) throws RemoteException {
		this.nom = nom;
		System.out.println( "Le "+ nom + " est en marche...");
	}
	
	private Object  getSyncForFile(String fichier) {
		synchronized(sync) {
			Object object = dico.get(fichier);
			if (object == null){
				object = new Object();
				dico.put(fichier, object);
			}
			return object;
		}
	}
	
	// Retourne le contenu du fichier texte
	public String lecture(String fichier) throws IOException {
		synchronized(this.getSyncForFile(fichier)) {
			System.out.println("Le " + this.nom + " commence la lecture fichier du " + fichier);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new FileReader(fichier));
			String texte = "";
			String texteFinal = "";
			String line;
			// Tant qu'il reste une ligne dans le fichier :
			while ((line = in.readLine()) != null){
				texteFinal = texte + line;
				texte = texteFinal;
			}
			in.close();
			System.out.println("Le " + this.nom + " finit la lecture fichier du " + fichier);
			return texteFinal;
		}
	}
	
	// Ecrit dans le fichier et retourne true si l'ecriture s'est bien passee
	public Boolean ecriture(String fichier, String donnees) throws IOException {
		synchronized(this.getSyncForFile(fichier)) {
			System.out.println("Le " + this.nom + " commence l'écriture fichier du " + fichier);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			FileWriter sortie = new FileWriter(fichier,true);
			sortie.write(donnees);
			sortie.close();
			System.out.println("Le " + this.nom + " finit l'écriture fichier du " + fichier);
			return true;
		}
	}
	
	public String getNom() throws RemoteException{
		return nom;
	}

	public static void main(String args[]) {
		try {
			// Creation de serveurs de type Machine
			Machine m1 = new Serveur("Serveur1");
			Machine m2 = new Serveur("Serveur2");
			
			// Exportation des serveurs et registration
			UnicastRemoteObject.exportObject(m1, 0);
			UnicastRemoteObject.exportObject(m2, 0);
			Registry registry = LocateRegistry.getRegistry("localhost");
			//enregistre serveurs 1 et 2 sur le registry
			registry.bind("Serveur1", m1);
			registry.bind("Serveur2", m2);

			
			} catch (Exception e) {
				System.err.println("Server exception: " + e.toString());
		    }
		System.out.println("End of main");
	}
}