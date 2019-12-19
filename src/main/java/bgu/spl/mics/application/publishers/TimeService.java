package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
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
	private Timer mrTime;
	private SimplePublisher x;




	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		mrTime = new Timer();
		x = this.getSimplePublisher();




	}

	@Override
	protected void initialize() {
		TimerTask tictac = new TimerTask() {
			@Override
			public void run() {
				TickBroadcast tick = new TickBroadcast(duration);
				x.sendBroadcast(tick);
			}
		};




		
	}

	@Override
	public void run() {
		while(duration!=0){
			TickBroadcast tick = new  TickBroadcast(duration);
			duration--;
			this.getSimplePublisher().sendBroadcast(tick);
		}
	}

}
