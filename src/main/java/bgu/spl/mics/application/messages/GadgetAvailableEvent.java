package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.myClasses.GadgetAvialableResult;

public class GadgetAvailableEvent implements Event<Integer> {

    private String gadget;
    private int currTick;


    public GadgetAvailableEvent(String gadget, int currTick) {
        this.gadget = gadget;
        this.currTick= currTick;
    }

    public String getGadget() {
        return gadget;
    }

    public int getCurrTick() {
        return currTick;
    }
}
