package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AboardMissionEvent implements Event<Boolean> {
    private List<String> agentsTORelease;

    public AboardMissionEvent(List<String> agentsTORelease) {
        this.agentsTORelease = agentsTORelease;
    }

    public List<String> getAgentsTORelease() {
        return agentsTORelease;
    }
}
