package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * GadgetAvailableEvent is an Event that M send to the MessageBroker and than the MessageBroker
 * send it to Q so he will handle it.
 * we contain in this event the gadget of the mission and the current tick.
 */
public class GadgetAvailableEvent implements Event<Integer> {

    private String gadget;
    private int currTick;

    /**
     * public constructor of this class.
     */
    public GadgetAvailableEvent(String gadget, int currTick) {
        this.gadget = gadget;
        this.currTick= currTick;
    }

    /**
     * Retrieves the gadget of the mission.
     */
    public String getGadget() {
        return gadget;
    }

}
