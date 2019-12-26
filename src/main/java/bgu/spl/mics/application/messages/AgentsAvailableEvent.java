package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.myClasses.AgentsAvialableResult;

import java.util.List;

public class AgentsAvailableEvent implements Event<AgentsAvialableResult> {

    private List<String> serials;
    private int duration;
    private int timeExpired;

    public AgentsAvailableEvent(List<String> serials, int duration, int endTime) {
        this.serials = serials;
        this.duration = duration;
        this.timeExpired = endTime;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }

    public int getTimeExpired() {
        return timeExpired;
    }
}
