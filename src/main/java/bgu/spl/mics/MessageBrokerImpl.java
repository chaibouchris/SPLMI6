package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

    // map of events an there future.
	private ConcurrentHashMap<Event, Future> eventFutureMap;
	// sub and the missions he toke.
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subscriberRegisterMap;
	// topic and its subs.
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> MessageSupPubMap;

	/**
	 * Retrieves the single instance of this class.
	 */

	public static class MessageBrokerHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}
	private MessageBrokerImpl(){
		eventFutureMap = new ConcurrentHashMap<>();
		subscriberRegisterMap = new ConcurrentHashMap<>();
		MessageSupPubMap = new ConcurrentHashMap<>();
	}
	public static MessageBrokerImpl getInstance() {
		return MessageBrokerHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if (!MessageSupPubMap.contains(type)) {// if topic doesnt exist then create it.
			MessageSupPubMap.put(type, new ConcurrentLinkedQueue<Subscriber>());
		}
		MessageSupPubMap.get(type).add(m);// add sub m to topic.
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if (!MessageSupPubMap.contains(type)){// if topic doesnt exist then create it.
			MessageSupPubMap.put(type, new ConcurrentLinkedQueue<Subscriber>());
		}
		MessageSupPubMap.get(type).add(m); // add sub m to topic
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);// simply resolve the future object.
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<Subscriber> queueSubs = getTopicSubs(b);
		for(Subscriber sub: queueSubs){
			BlockingQueue<Message> registerQ = getSubQueue(sub);
			try {
				registerQ.put(b);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private BlockingQueue<Message> getSubQueue(Subscriber sub) {
		return subscriberRegisterMap.get(sub);
	}

	private ConcurrentLinkedQueue<Subscriber> getTopicSubs(Broadcast b) {
		return MessageSupPubMap.get(b.getClass());
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		ConcurrentLinkedQueue<Subscriber> queueSubs = MessageSupPubMap.get(e.getClass());
		Future<T> send = new Future<>();
		try {
			Subscriber sub = queueSubs.remove();
			BlockingQueue<Message> Qregister = subscriberRegisterMap.get(sub);
			Qregister.put(e);
			eventFutureMap.put(e, send);
			queueSubs.add(sub);
		} catch (Exception excep){
			excep.printStackTrace();
		}
		return send;
	}

	@Override
	public void register(Subscriber m) {
		if (!subscriberRegisterMap.contains(m)){
			subscriberRegisterMap.put(m, new LinkedBlockingQueue<>());
		}
	}

	@Override
	public void unregister(Subscriber m) {
		if (subscriberRegisterMap.remove(m) != null) {
			for (Map.Entry<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> iter : MessageSupPubMap.entrySet()) {
				ConcurrentLinkedQueue<Subscriber> subQ = iter.getValue();
				if (subQ.contains(m)){
					subQ.remove(m);
				}
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		BlockingQueue<Message> Q = subscriberRegisterMap.get(m);
		return Q.take();
	}

	

}
