package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.myClasses.AgentsAvialableResult;
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
		subscribeBrod();
		subscribeTerminateBrod();
		subscribeMissionRecieved();
	}

	private void subscribeMissionRecieved() {
		subscribeEvent(MissionReceivedEvent.class, (E) -> {
			diaryOfJane.incrementTotal();

			AgentsAvailableEvent AAE = new AgentsAvailableEvent(E.getSerials(), E.getDuration(), E.getExpiredTime());
			Future<AgentsAvialableResult> future = getSimplePublisher().sendEvent(AAE);
			System.out.println(Thread.currentThread().getName()+" send agentAvialable");

			long timeOut = (E.getExpiredTime() - currTick)*100;
			AgentsAvialableResult AAR = future.get(timeOut , TimeUnit.MILLISECONDS);
			Future MgetGadget;

			if (AAR != null && AAR.getGetAgents()) {
				GadgetAvailableEvent GAE = new GadgetAvailableEvent(E.getGadget(), currTick);
				Future<Integer> gadgetF = getSimplePublisher().sendEvent(GAE);
				System.out.println(Thread.currentThread().getName()+" send gadgetAvialable");
				Integer foundGadget = gadgetF.get(timeOut , TimeUnit.MILLISECONDS);
				complete(GAE, foundGadget);
				System.out.println(this.getName()+id +"complete event");

				if (foundGadget != null && currTick <= E.getExpiredTime() && foundGadget != -1) {
					MgetGadget = AAR.getGetGadget();
					MgetGadget.resolve(true);
					int qTime = gadgetF.get();
					List<String> agentsName = AAR.getAgentsNames();
					List<String> serials = E.getSerials();
					int mPennyId = AAR.getMoneyPennyID();
					String nameOfMission = E.getMissionInfo().getMissionName();
					writeReport(serials, E.getTimeIssued(), agentsName, qTime, mPennyId, E.getGadget(), nameOfMission);
				} else {
					MgetGadget = AAR.getGetGadget();
					MgetGadget.resolve(false);
				}//no agents avialable or time expired
			}
			complete(AAE, AAR);
		});
	}

	private void subscribeTerminateBrod() {
		subscribeBroadcast(TerminateBroadcast.class, (TB) -> {
			terminate();
			System.out.println(this.getName()+id+"unregister");
		});
	}

	private void subscribeBrod() {
		subscribeBroadcast(TickBroadcast.class, (B) -> {
			setCurrTick(B.getTick());
		});
	}

	private void writeReport(List<String> serials, int timeIssued, List<String> agentsName, int qtime, int MpID, String gadget, String nameOfmission){
		Report toAdd = new Report();
		toAdd.setQTime(qtime);
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



