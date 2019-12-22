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

    // map of events and future.
	private ConcurrentHashMap<Event, Future> eventFutureMap;
	// sub and the missions he toke.
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subscriberRegisterMap;
	// topic and its subs.
	private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> MessageSupPubMap;

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
		// if topic doesnt exist then create it.
			MessageSupPubMap.putIfAbsent(type, new LinkedBlockingQueue<Subscriber>());

		MessageSupPubMap.get(type).add(m);// add sub m to topic.
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// if topic doesnt exist then create it.
			MessageSupPubMap.putIfAbsent(type, new LinkedBlockingQueue<>());

		MessageSupPubMap.get(type).add(m); // add sub m to topic
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);// simply resolve the future object.
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		LinkedBlockingQueue<Subscriber> queueSubs = getTopicSubs(b);
		if(queueSubs==null)
			return;
		for(Subscriber sub: queueSubs){
			BlockingQueue<Message> registerQ = getSubQueue(sub);
			try {
				registerQ.add(b);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private BlockingQueue<Message> getSubQueue(Subscriber sub) {
		return subscriberRegisterMap.get(sub);
	}

	private LinkedBlockingQueue<Subscriber> getTopicSubs(Broadcast b) {
		return MessageSupPubMap.get(b.getClass());
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		BlockingQueue<Subscriber> queueSubs = MessageSupPubMap.get(e.getClass());
		Future<T> send = new Future<>();
		try {
			if (queueSubs == null)
				return null;
			eventFutureMap.put(e, send);
			Subscriber sub = queueSubs.remove();
			BlockingQueue<Message> Qregister = subscriberRegisterMap.get(sub);
			Qregister.add(e);
			queueSubs.add(sub);
		} catch (Exception excep){
			excep.printStackTrace();
		}
		return send;
	}

	@Override
	public void register(Subscriber m) {

			subscriberRegisterMap.putIfAbsent(m, new LinkedBlockingQueue<>());

	}

	@Override
	public void unregister(Subscriber m) {
		// remove m from the subscriberRegisterMap
		if (subscriberRegisterMap.remove(m) != null) {
			// if m was in the subscriberRegisterMap  then remove it from each topic he was register to.
			for (Map.Entry<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> iter : MessageSupPubMap.entrySet()) {
				LinkedBlockingQueue<Subscriber> subQ = iter.getValue();
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
