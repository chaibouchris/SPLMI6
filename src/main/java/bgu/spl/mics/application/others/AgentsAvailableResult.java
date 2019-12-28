package bgu.spl.mics.application.others;

import bgu.spl.mics.Future;

import java.util.List;

/**
 * this class was made for being the result of the AgentAvailableEvent so we can analyze from the event
 * if the mission will be on the way or get canceled.
 * it "contains" the information of: the id of the Moneypenny that check the agents, if we got the agents, the name
 * of those agents, and a future of the gadget for the mission
 */
public class AgentsAvailableResult {

    private  int MoneyPennyID;
    private Boolean getAgents;
    private List<String> agentsNames;
    private Future<Boolean> getGadget;

    /**
     * public constructor of this class.
     */
    public AgentsAvailableResult(int MP, Boolean getAgents, List<String> aN, Future<Boolean> future){
        this.MoneyPennyID = MP;
        this.getAgents = getAgents;
        this.agentsNames = aN;
        this.getGadget = future;
    }

    /**
     * Retrieves the MoneyPenny ID of the AgentAvailableEvent.
     */
    public int getMoneyPennyID() {
        return MoneyPennyID;
    }

    /**
     * Retrieves if we got the agents.
     */
    public Boolean weGotTheAgents() {
        return getAgents;
    }

    /**
     * Retrieves a list of the names of the agents in the mission.
     */
    public List<String> getAgentsNames() {
        return agentsNames;
    }

    /**
     * Retrieves the future of if we got the gadget of the mission.
     */
    public Future<Boolean> weGotTheGadget() {
        return getGadget;
    }
}
