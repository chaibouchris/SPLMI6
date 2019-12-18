package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private Diary anaFrank;

	public M() {
		super("M");
		anaFrank = Diary.getInstance();
	}

	@Override
	protected void initialize() {
		MessageBrokerImpl.getInstance().register(this);
	}

}
