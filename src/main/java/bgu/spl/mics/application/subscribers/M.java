package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.AgentsAvialableResult;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.GadgetAvialableResult;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber  {

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
		MessageBrokerImpl.getInstance().register(this);

		subscribeBroadcast(TickBroadcast.class, (B) ->{
			setCurrTick(B.getTick());
		});

		subscribeBroadcast(TerminateBroadcast.class, (TB) ->{
			MessageBrokerImpl.getInstance().unregister(this);
			terminate();
		});

		subscribeEvent(MissionReceivedEvent.class,(E)->{
			anaFrank.incrementTotal();

			MissionInfo MI = E.getMissionInfo();
			SimplePublisher pubi = getSimplePublisher();
			List<String> serials = MI.getSerialAgentsNumbers();
			int duration = MI.getDuration();

			Future<AgentsAvialableResult> AAR = pubi.sendEvent(new AgentsAvailableEvent(serials));
			if (AAR == null){
				MessageBrokerImpl.getInstance().unregister(this);
				terminate();
				return;
			}
			if (AAR.get() == null || !AAR.get().getResult()){
				return;
			}

			Future<GadgetAvialableResult> gadgetAvaiable = pubi.sendEvent(new GadgetAvailableEvent(MI.getGadget()));
			if (gadgetAvaiable == null){
				MessageBrokerImpl.getInstance().unregister(this);
				terminate();
				return;
			}
			if (gadgetAvaiable.get() == null || !gadgetAvaiable.get().getResult()){
				ReleaseAgents(pubi, serials);
				return;
			}

			GadgetAvialableResult result = gadgetAvaiable.get();
			if (result.getTime() > MI.getTimeExpired()){
				ReleaseAgents(pubi, serials);
				return;
			}

			Future<Boolean> UTB = pubi.sendEvent(new UnleashTheBeastEvent(serials, duration));
			if (UTB == null){
				ReleaseAgents(pubi, serials);
				return;
			}

			complete(E, true);

		});
	}

	public void setCurrTick(int toSet){
		this.currTick = toSet;
	}

	private void ReleaseAgents(SimplePublisher pubi, List<String> serials){
		Future<Boolean> BB = pubi.sendEvent(new AboardMissionEvent(serials));
		while (BB != null && BB.get() == null){
			BB = pubi.sendEvent(new AboardMissionEvent(serials));
	}

private GadgetAvailableEvent createGadgetEvent(MissionReceivedEvent event){
	 return new GadgetAvailableEvent(event.getMissionInfo().getGadget());
}
private AgentsAvailableEvent createAgentEvent(MissionReceivedEvent event){
	return new AgentsAvailableEvent(event.getMissionInfo().getSerialAgentsNumbers());
}

private AboardMissionEvent creatAboardEvent(MissionReceivedEvent event){
		return new AboardMissionEvent(event.getMissionInfo().getSerialAgentsNumbers());
}

}
