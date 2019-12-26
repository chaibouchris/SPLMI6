package bgu.spl.mics.application.myClasses;

import bgu.spl.mics.Future;

import java.util.List;

public class AgentsAvialableResult {

    private  int MoneyPennyID;
    private Boolean getAgents;
    private List<String> agentsNames;
    private Future<Boolean> getGadget;

    public AgentsAvialableResult(int MP, Boolean getAgents, List<String> aN, Future<Boolean> future){
        this.MoneyPennyID = MP;
        this.getAgents = getAgents;
        this.agentsNames = aN;
        this.getGadget = future;
    }


    public int getMoneyPennyID() {
        return MoneyPennyID;
    }

    public Boolean getGetAgents() {
        return getAgents;
    }

    public List<String> getAgentsNames() {
        return agentsNames;
    }

    public Future<Boolean> getGetGadget() {
        return getGadget;
    }
}
