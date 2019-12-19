package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber  {

	private Diary anaFrank;
	private int id;

	public M(int id) {
		super("M");
		anaFrank = Diary.getInstance();
		this.id = id;
	}

	@Override
	protected void initialize() {

		MessageBrokerImpl.getInstance().register(this);
		subscribeEvent(MissionReceivedEvent.class,(E)->{
		Future gadget = this.simplePublisher.sendEvent(createAgentEvent(E));
		while (!gadget.isDone())// wait for gadget
		if(!(boolean)gadget.getResult()){
			this.simplePublisher.sendEvent(creatAboardEvent(E));
		}
		else{
			this.simplePublisher.sendEvent(createGadgetEvent(E));
		}

		});//should aboard mission in case time end
		   // idea : take time of geting mission and a counter of ticks ...
		   // for each TickB counter++;
		   // if counter = mission duretion - aborad !!!!
		subscribeBroadcast(TickBroadcast.class,(T)->{

		});
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
