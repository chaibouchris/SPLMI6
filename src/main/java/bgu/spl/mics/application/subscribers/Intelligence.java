package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

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
		sortByTimeIssued();//we want that they will be from the first time issued to the last
	}


	@Override
	protected void initialize() {
		SimplePublisher pubi = getSimplePublisher();
		subscribeBrod(pubi);//subscribe himself for broadcasts
		subscribeTerminateBrod();//subscribe himself for the terminate broadcast
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			terminate();
		});
	}

	private void subscribeBrod(SimplePublisher pubi) {
		subscribeBroadcast(TickBroadcast.class, (E)->{
			setCurrTick(E.getTick());
			if (!theList.isEmpty()) {
				MissionInfo MI = theList.get(0);//get the first mission in the list
				while (currTick == MI.getTimeIssued() && !theList.isEmpty()) {//want to send all the missions with this time issued
					pubi.sendEvent(new MissionReceivedEvent(MI));//send mission receive event
					theList.remove(0);//remove the mission from the list
					if (!theList.isEmpty())
					MI = theList.get(0);//if we have more missions we check if it we need to send it now
				}
			}
		});
	}

	private void sortByTimeIssued(){//sort the missions form first time issued to last
		theList.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}

}
