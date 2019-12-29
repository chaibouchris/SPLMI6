package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory invi;
	private int currTick;


	public Q() {
		super("Q");
		invi = Inventory.getInstance();
		currTick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBrod();//subscribe himself for the broadcasts
		subscribeTerminateBrod();//subscribe himself for the terminate broadcast
		subscribeGadgetAvailableEvent();// subscribe himself for the gadget avialable event
	}

	private void subscribeGadgetAvailableEvent() {
		subscribeEvent(GadgetAvailableEvent.class, (E) -> {
			boolean booli = invi.getItem(E.getGadget());
			if (booli){
				complete(E, currTick);//with the time he get it
			} else{
				complete(E, -1);//with -1 if not get it
			}
		});
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			terminate();
		});
	}

	private void subscribeBrod() {
		subscribeBroadcast(TickBroadcast.class, (B) ->{
			setCurrTick(B.getTick());
		});
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}
}