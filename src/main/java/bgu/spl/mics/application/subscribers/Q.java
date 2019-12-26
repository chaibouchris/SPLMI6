package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.myClasses.GadgetAvialableResult;
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
		subscribeBrod();
		subscribeTerminateBrod();

		subscribeEvent(GadgetAvailableEvent.class, (E) -> {
			GadgetAvialableResult GAR = new GadgetAvialableResult(invi.getItem(E.getGadget()), currTick);
			complete(E, GAR);
		});
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			MessageBrokerImpl.getInstance().unregister(this);
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