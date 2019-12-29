package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.others.AgentsAvailableResult;

import java.util.List;

/**
 * AgentsAvailableEvent is an Event that M send to the MessageBroker and than the MessageBroker
 * send to the Moneypenney's so one of them will handle it.
 * we contain in this event the list of serials of the agents of this mission, the duration
 * of the mission and the expired time of this mission.
 */
public class AgentsAvailableEvent implements Event<AgentsAvailableResult> {

    private List<String> serials;
    private int duration;
    private int timeExpired;

    /**
     * public constructor of this class.
     */
    public AgentsAvailableEvent(List<String> serials, int duration, int endTime) {
        this.serials = serials;
        this.duration = duration;
        this.timeExpired = endTime;
    }

    /**
     * Retrieves the list of the serials of the agents.
     */
    public List<String> getSerials() {
        return serials;
    }

    /**
     * Retrieves the duration of the mission.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Retrieves the expired time of the mission.
     */
    public int getTimeExpired() {
        return timeExpired;
    }
}
