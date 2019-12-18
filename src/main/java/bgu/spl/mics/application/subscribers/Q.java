package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory invi;


	private static class QHolder {
		private static Q instance = new Q();
	}
	public static Q getInstance() {
		return Q.QHolder.instance;
	}


	public Q() {
		super("Q");
		invi = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		
	}

}
