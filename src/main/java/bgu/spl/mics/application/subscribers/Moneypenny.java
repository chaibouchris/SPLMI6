package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.myClasses.AgentsAvialableResult;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
		subscribeBroadcast(TickBroadcast.class, (B) ->{
			setCurrTick(B.getTick());
		});

		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			MessageBrokerImpl.getInstance().unregister(this);
			terminate();
		});

		subscribeEvent(AgentsAvailableEvent.class, (E) -> {
			int timeExpired = E.getTimeExpired();
			List<String> serials = E.getSerials();
			Boolean getAgents = saqi.getAgents(serials);
			List<String> agentsNames = new ArrayList<>();
			if (getAgents){
				agentsNames = saqi.getAgentsNames(serials);
			}

			Future<Boolean> gadgetFuture = new Future<>();
			AgentsAvialableResult AAR = new AgentsAvialableResult(this.id, getAgents, agentsNames, gadgetFuture);
			complete(E, AAR);
			Boolean gotGadget = gadgetFuture.get((timeExpired = currTick)*100, TimeUnit.MILLISECONDS);

			if (gotGadget != null && gotGadget.booleanValue() == true && getAgents){
				saqi.sendAgents(serials, E.getDuration());
			} else saqi.releaseAgents(serials);
		});

	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}

}
