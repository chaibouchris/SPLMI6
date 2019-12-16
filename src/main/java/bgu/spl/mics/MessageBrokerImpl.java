package bgu.spl.mics;

import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private ConcurrentHashMap<Event, Future> eventFutureMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subscriberRegisterMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> MessageSupPubMap = new ConcurrentHashMap<>();

	/**
	 * Retrieves the single instance of this class.
	 */

	private MessageBrokerImpl(){
	}

	private static class MessageBrokerHolder {
		private static MessageBroker instance = new MessageBrokerImpl();
	}
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if (!MessageSupPubMap.contains(type))
			MessageSupPubMap.put(type, new ConcurrentLinkedQueue<Subscriber>());
		ConcurrentLinkedQueue<Subscriber> queueSubscribers = MessageSupPubMap.get(type);
		queueSubscribers.add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if (!MessageSupPubMap.contains(type))
			MessageSupPubMap.put(type, new ConcurrentLinkedQueue<Subscriber>());
		ConcurrentLinkedQueue<Subscriber> queueSubscribers = MessageSupPubMap.get(type);
		queueSubscribers.add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<Subscriber> queueSubs = MessageSupPubMap.get(b.getClass());
		for(Subscriber sub: queueSubs){
			BlockingQueue<Message> registerQ = subscriberRegisterMap.get(sub);
			try {
				registerQ.put(b);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
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
