package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.myClasses.AgentsAvialableResult;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.myClasses.GadgetAvialableResult;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
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

	private Diary anaFrank;
	private int id;
	int currTick;

	public M(int id) {
		super("M");
		anaFrank = Diary.getInstance();
		this.id = id;
		currTick = 0;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (B) -> {
			setCurrTick(B.getTick());
		});

		subscribeBroadcast(TerminateBroadcast.class, (TB) -> {
			MessageBrokerImpl.getInstance().unregister(this);
			terminate();
		});

		subscribeEvent(MissionReceivedEvent.class, (E) -> {
			anaFrank.incrementTotal();

			AgentsAvailableEvent AAE = new AgentsAvailableEvent(E.getSerials(), E.getDuration(), E.getEndTime());
			Future<AgentsAvialableResult> future = getSimplePublisher().sendEvent(AAE);

			AgentsAvialableResult AAR = future.get((E.getEndTime() - currTick) * 100, TimeUnit.MILLISECONDS);
			Future MgetGadget;

			if (AAR != null && AAR.getGetAgents()) {
				GadgetAvailableEvent GAE = new GadgetAvailableEvent(E.getGadget(), currTick);
				Future<Integer> gadgetF = getSimplePublisher().sendEvent(GAE);
				Integer foundGadget = gadgetF.get((E.getEndTime() - currTick) * 100, TimeUnit.MILLISECONDS);
				complete(GAE, foundGadget);

				if (foundGadget != null && currTick <= E.getEndTime() && foundGadget != -1) {
					MgetGadget = AAR.getGetGadget();
					MgetGadget.resolve(true);
					int qTime = gadgetF.get();
					List<String> agentsName = AAR.getAgentsNames();
					List<String> serials = E.getSerials();
					int mPennyId = AAR.getMoneyPennyID();
					writeReport(serials, E.getTimeIssued(), agentsName, qTime, mPennyId, E.getGadget());
				} else if (AAR != null) {
					MgetGadget = AAR.getGetGadget();
					MgetGadget.resolve(false);
				}//no agents avialable ot time expired
			}
			complete(AAE, AAR);
		});
	}

	private void writeReport(List<String> serials, int timeIssued, List<String> agentsName, int qtime, int MpID, String gadget){
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
		anaFrank.addReport(toAdd);
	}

	public void setCurrTick(int toSet) {
		this.currTick = toSet;
	}
}



