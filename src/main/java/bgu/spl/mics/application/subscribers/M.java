package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.others.AgentsAvailableResult;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private Diary diaryOfJane;
	private int id;
	int currTick;

	public M(int id) {
		super("M");
		diaryOfJane = Diary.getInstance();
		this.id = id;
		currTick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBrod();//subscribe himself for the broadcasts
		subscribeTerminateBrod();//subscribe himself for the terminate broadcasts
		subscribeMissionReceived();//subscribe himself for the mission received
	}

	private void subscribeMissionReceived() {
		subscribeEvent(MissionReceivedEvent.class, (E) -> {
			diaryOfJane.incrementTotal();//add it to the diary
			AgentsAvailableEvent AAE = new AgentsAvailableEvent(E.getSerials(), E.getDuration(), E.getExpiredTime());
			Future<AgentsAvailableResult> futureAAR = getSimplePublisher().sendEvent(AAE);
			//send  agent available event (that will deliver to moneypenny)
			int expiredTime = E.getExpiredTime();
			AgentsAvailableResult AAR = futureAAR.get((expiredTime - currTick + 1)*100 , TimeUnit.MILLISECONDS);
			Future futureMgotTheGadget;
			if (AAR != null && AAR.weGotTheAgents()) {//enter if we get the agents
				GadgetAvailableEvent GAE = new GadgetAvailableEvent(E.getGadget(), currTick);
				Future<Integer> gadgetFuture = getSimplePublisher().sendEvent(GAE);//send gadget available event
				Integer foundGadget = gadgetFuture.get((expiredTime - currTick + 1)*100 , TimeUnit.MILLISECONDS);
				complete(GAE, foundGadget);//the event with the result that give the time we got the gadget/-1 if we dont get it

				if (foundGadget != null && currTick <= expiredTime && foundGadget != -1) {//if we got the gadget and the time isn't expired
					futureMgotTheGadget = AAR.weGotTheGadget();
					futureMgotTheGadget.resolve(true);
					int qTime = gadgetFuture.get();//when q get the gadget
					List<String> agentsName = AAR.getAgentsNames();
					List<String> serials = E.getSerials();
					int mPennyId = AAR.getMoneyPennyID();
					String nameOfMission = E.getMissionInfo().getMissionName();
					writeReport(serials, E.getTimeIssued(), agentsName, qTime, mPennyId, E.getGadget(), nameOfMission);
				} else {
					futureMgotTheGadget = AAR.weGotTheGadget();
					futureMgotTheGadget.resolve(false);
				}//no agents avialable or time expired
			}
			complete(AAE, AAR);
		});
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) -> {
			terminate();
		});
	}

	private void subscribeBrod() {
		subscribeBroadcast(TickBroadcast.class, (B) -> {
			setCurrTick(B.getTick());
		});
	}

	private void writeReport(List<String> serials, int timeIssued, List<String> agentsName, int qtime, int MpID, String gadget, String nameOfmission){
		Report toAdd = new Report();
		toAdd.setQtime(qtime);
		toAdd.setAgentsNames(agentsName);
		toAdd.setTimeIssued(timeIssued);
		toAdd.setGadgetName(gadget);
		toAdd.setMoneypenny(MpID);
		toAdd.setM(this.id);
		toAdd.setMoneypenny(MpID);
		toAdd.setTimeCreated(currTick);
		toAdd.setAgentsSerialNumbersNumber(serials);
		toAdd.setMissionName(nameOfmission);
		diaryOfJane.addReport(toAdd);
	}

	public void setCurrTick(int toSet) {
		this.currTick = toSet;
	}
}



