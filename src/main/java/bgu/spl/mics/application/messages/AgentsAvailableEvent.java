package bgu.spl.mics.application.messages;

import java.util.List;

public class AgentsAvailableEvent {

    private List<String> serials;

    public AgentsAvailableEvent(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getSerials() {
        return serials;
    }
}
