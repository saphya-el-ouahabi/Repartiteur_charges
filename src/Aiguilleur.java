import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Aiguilleur implements AiguilleurInterface {
	//hashtable => dictionnaire en cle une machine, valeur un entier
	private  Hashtable<Machine, Integer> dicoCharge = new Hashtable<Machine,Integer>();
	private ArrayList<Machine> listeMachine = new ArrayList<Machine>();
	private String nom;
	
	public Aiguilleur(String nom) throws IOException {
		this.nom=nom;
	}

	public String lecture(String donnees) throws RemoteException, FileNotFoundException, IOException {
		Machine machine = this.getMachine();
		System.out.println("Aiguilleur " + machine.getNom() + " lecture fichier = " + donnees);
		String result = machine.lecture(donnees);
		releaseMachine(machine);
		return ""+machine.getNom()+" : "+result;
	}

	public Boolean ecriture(String nom, String donnees) throws RemoteException, FileNotFoundException, IOException {
		Machine machine = this.getMachine();
		System.out.println("Aiguilleur " + machine.getNom() + " écriture fichier = " + nom);
		Boolean result = machine.ecriture(nom,""+machine.getNom()+" : "+donnees);
		releaseMachine(machine);
		return result;
	}
	//ajoute une machine avec 0
	//0 => charge
	public boolean addMachine(Machine m) throws RemoteException {
		this.dicoCharge.put(m,0);
		return true;
	}

	public boolean removeMachine(Machine m) throws RemoteException {
		this.dicoCharge.remove(m);
		return true;
	}

	@Override
	public boolean notification(Machine m, int a) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	// L'aiguilleur boucle parmi les serveurs en recuperant celui avec la plus petite charge
	private synchronized Machine getMachine() throws RemoteException {
		int num = 10000;
		Machine m = null;
		Set<Machine> keys = dicoCharge.keySet();
		for (Machine machine : keys){
			if (num> dicoCharge.get(machine) ){
				num=dicoCharge.get(machine);
				m = machine;
			}
		}
		incrementeCharge(m);
		return m;
	}
	
	private void incrementeCharge(Machine m) throws RemoteException{
		int charge = dicoCharge.get(m)+1;
		this.dicoCharge.put(m, charge);
	}
	
	private synchronized void releaseMachine(Machine  m) throws RemoteException{
		int charge = dicoCharge.get(m)-1;
		this.dicoCharge.put(m, charge);
	}
	
	public String getNom() throws RemoteException{
		return nom;
	}
	
    public void registerClient(Client client) throws RemoteException {
		System.out.println("Aiguilleur registerClient");
    }

    public static void main(String args[]) {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
			//cherche dans le registry les serveurs
			Machine m1 = (Machine) registry.lookup("Serveur1");
			Machine m2 = (Machine) registry.lookup("Serveur2");
			// Creation d'un aiguilleur de type Machine						
			Aiguilleur a1 = new Aiguilleur("Aiguilleur");
			a1.addMachine(m1);
			a1.addMachine(m2);
								
			// Exportation de l'aiguilleur et registration
			UnicastRemoteObject.exportObject(a1, 0);
			registry.bind("Aiguilleur", a1);
			
			} catch (Exception e) {
				System.err.println("Server exception: " + e.toString());
		    }
		System.out.println("End of main");
	}
}