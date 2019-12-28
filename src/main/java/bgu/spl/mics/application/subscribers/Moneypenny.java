package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.others.AgentsAvailableResult;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;
import java.util.LinkedList;
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
	private List<String> agentsInService;

	public Moneypenny(int id) {
		super("MoneyPenny");
		saqi = Squad.getInstance();
		this.id = id;
		this.currTick = 0;
		agentsInService = new LinkedList<>();
	}

	@Override
	protected void initialize() {
		subscribeBrod();//subscribe himself for the broadcasts
		subscribeTerminateBrod();//subscribe himself for the terminate broadcasts
		subscribeAgentsAvailableEvent();//subscribe himself for agents available event
	}

	private void subscribeAgentsAvailableEvent() {
		subscribeEvent(AgentsAvailableEvent.class, (E) -> {
			int timeExpired = E.getTimeExpired();
			List<String> serials = E.getSerials();
			agentsInService.addAll(serials);//to know what agents im using now
			Boolean getAgents = saqi.getAgents(serials);
			List<String> agentsNames = new ArrayList<>();
			if (getAgents){
				agentsNames = saqi.getAgentsNames(serials);//only need the names when i got them
			}
			Future<Boolean> gadgetFuture = new Future<>();//the future we use for the gadget
			AgentsAvailableResult AAR = new AgentsAvailableResult(this.id, getAgents, agentsNames, gadgetFuture);
			complete(E, AAR);//save the id, if i got the agents, the name of them and the future of thr gadget
			Boolean isGotGadget = gadgetFuture.get((timeExpired - currTick + 1)*100, TimeUnit.MILLISECONDS);

			if (isGotGadget != null && isGotGadget && getAgents){
				saqi.sendAgents(serials, E.getDuration());//we send them if everything like we want
			} else {
				saqi.releaseAgents(serials);//release them because something is not ok
			}
		});
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			saqi.releaseAgents(agentsInService);
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
