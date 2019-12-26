package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> theList;
	private int id;
	private int currTick;

	public Intelligence(int id, List<MissionInfo> MisList) {
		super("Intelligence");
		this.id = id;
		currTick = 0;
		this.theList = MisList;
		sortByTimeIssued();
	}

	@Override
	protected void initialize() {
		SimplePublisher pubi = getSimplePublisher();
		subscribeBrod(pubi);
		subscribeTerminateBrod();
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			MessageBrokerImpl.getInstance().unregister(this);
			terminate();
		});
	}

	private void subscribeBrod(SimplePublisher pubi) {
		subscribeBroadcast(TickBroadcast.class, (E)->{
			MissionInfo MI = theList.get(0);
			setCurrTick(E.getTick());
			if (currTick == MI.getTimeIssued()){
				pubi.sendEvent(new MissionReceivedEvent(MI));
				theList.remove(0);
			}
		});
	}

	private void sortByTimeIssued(){
		theList.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}

}
