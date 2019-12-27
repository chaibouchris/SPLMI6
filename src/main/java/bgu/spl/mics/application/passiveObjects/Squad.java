package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private AtomicBoolean notTerminate = new AtomicBoolean(true);

	/**
	 * Retrieves the single instance of this class.
	 */


	public static class SquadHolder {
		private static Squad instance = new Squad();
	}
	private Squad(){
		agents = new ConcurrentHashMap<>();
	}
	public static Squad getInstance() {
		return SquadHolder.instance;
	}


	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent x: agents) {
			this.agents.put(x.getSerialNumber(),x);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		Collections.sort(serials);
		for (String x:serials) {
			if(this.agents.containsKey(x)){
				agents.get(x).release();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		try {
			Thread.sleep(100*time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		Collections.sort(serials);
		boolean everybodyHere = true;// if some one is missing then i want to acqurie everbody else and return true.
		for (String x: serials) {
			everybodyHere = this.agents.containsKey(x);
			if (!everybodyHere)
				break;
		}
		if (everybodyHere) {
			for (String x : serials) {
				agents.get(x).acquire();
			}
		}

		return everybodyHere;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
    	List<String> agentsNames = new ArrayList<>();
    	for(String serialNums:serials) {
			String name = agents.get(serialNums).getName();
			agentsNames.add(name);
		}
	    return agentsNames;
    }
}
