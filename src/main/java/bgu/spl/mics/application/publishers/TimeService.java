package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import bgu.spl.mics.application.messages.TickBroadcast;


import java.util.Timer;
import java.util.TimerTask;



/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int duration;
	private long delay = 100;
	private SimplePublisher x;
	private Timer mrTime;




	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		x = this.getSimplePublisher();
		mrTime = new Timer();


	}

	@Override
	protected void initialize() {
		run();
	}

	@Override
	public void run() {
     mrTime.schedule(new TimerTask() {
		 @Override
		 public void run() {
			if(duration==0){// if we reached the final tick terminate all!!!
				TerminateBroadcast Terminate = new TerminateBroadcast();
				x.sendBroadcast(Terminate);
				mrTime.cancel();
				mrTime.purge();
			}
			else{// else send a tick
				TickBroadcast ticky = new TickBroadcast(duration);
				x.sendBroadcast(ticky);
				duration--; // update countdown to terminate
			}
		 }
	 },delay);

	}
}
