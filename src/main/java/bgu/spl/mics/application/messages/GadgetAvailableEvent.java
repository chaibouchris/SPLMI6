package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.GadgetAvialableResult;

public class GadgetAvailableEvent implements Event<GadgetAvialableResult> {

    private String gadget;

    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }
}
