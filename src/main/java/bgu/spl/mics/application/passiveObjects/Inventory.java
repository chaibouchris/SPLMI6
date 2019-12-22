package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;

	/**
     * Retrieves the single instance of this class.
     */

	public static class InventoryHolder {
		private static Inventory instance = new Inventory();
	}
	private Inventory(){
		gadgets = new ArrayList<>();
	}
	public static Inventory getInstance() {
		return InventoryHolder.instance;
	}


	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for (String x:inventory) {// just a for each loop to add all the beautiful gadgets to the Q list.
			gadgets.add(x);
		}
	}

	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public synchronized boolean getItem(String gadget){// synchronized cause i dont want to missions to acquire the same gadget at the same time.
		if(this.gadgets.contains(gadget)){
			this.gadgets.remove(gadget);
			return true;
		}
		else return false;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		Gson gisi = new Gson();
		String output = gisi.toJson(gadgets);
		try {
			Writer amos = new FileWriter(filename);
			amos.write(output);
		} catch (IOException e) {
			System.out.println("illegal filename");
		}
	}
}
