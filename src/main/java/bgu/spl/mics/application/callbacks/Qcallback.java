package bgu.spl.mics.application.callbacks;

import bgu.spl.mics.Callback;
import bgu.spl.mics.application.passiveObjects.Inventory;

public class Qcallback implements Callback {
    private boolean isInInvantory = false;

    public void call(String gadgets) {
        isInInvantory = Inventory.getInstance().getItem(gadgets);
    }

    @Override
    public void call(Object c) {

    }
}
