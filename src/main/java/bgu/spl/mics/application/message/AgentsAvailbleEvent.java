package bgu.spl.mics.application.message;

import java.util.List;

public class AgentsAvailbleEvent {
    private List<String> agents;

    AgentsAvailbleEvent(List<String> agents){
        this.agents = agents;
    }

    private List<String> getAgents() {
        return agents;
    }
}
