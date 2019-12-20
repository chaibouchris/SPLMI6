package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private Squad saqi;
	private int id;
	private int currTick;

	public Moneypenny(int id) {
		super("MoneyPenny");
		saqi = Squad.getInstance();
		this.id = id;
		this.currTick = 0;
	}

	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);

		subscribeBroadcast(TickBroadcast.class, (B) ->{
			setCurrTick(B.getTick());
		});

		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			MessageBrokerImpl.getInstance().unregister(this);
			terminate();
		});

		subscribeEvent(AgentsAvailableEvent.class, (E) -> {
			List<String> serials = E.getSerials();
			boolean result = saqi.getAgents(serials);
			complete(E, result);
		});
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}

}
