package bgu.spl.mics.application.messages;

public class GadgetAvailableEvent {

    private String gadget;

    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }
}
