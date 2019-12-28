package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import bgu.spl.mics.application.messages.TickBroadcast;




/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int duration;
	private int currTick;


	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		currTick = 0;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		while (currTick < duration){//so we still have ticks to send
			TickBroadcast TB = new TickBroadcast(currTick);
			getSimplePublisher().sendBroadcast(TB);//send the broadcast
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currTick++;
		}//when finish the ticks we send broadcast to terminate
		TerminateBroadcast finito = new TerminateBroadcast();
		getSimplePublisher().sendBroadcast(finito);//send terminate broadcast
	}
}
