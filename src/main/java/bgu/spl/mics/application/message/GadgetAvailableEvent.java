package bgu.spl.mics.application.message;

public class GadgetAvailableEvent {
    private String gadget;

    GadgetAvailableEvent(String gadget){
        this.gadget=gadget;
    }

    public String getGadget() {
        return gadget;
    }
}
