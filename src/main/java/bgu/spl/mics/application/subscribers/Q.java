package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
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
		MessageBrokerImpl.getInstance().register(this);
		subscribeBroadcast(TickBroadcast.class, (B) ->{
			setCurrTick(B.getTick());
		});

		subscribeBroadcast();
		subscribeEvent(GadgetAvailableEvent.class, (E) -> {
			if (invi.getItem(E.getGadget())) {
				complete(E, true);
			} else {
				complete(E, false);
			}

		});
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}
}