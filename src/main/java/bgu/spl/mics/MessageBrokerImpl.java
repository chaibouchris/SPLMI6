package bgu.spl.mics;

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
    // subscribers and the missions he toke.
    private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subscriberRegisterMap;
    // topic and its subs.
    private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> MessageSupPubMap;

    /**
     * Retrieves the single instance of this class.
     */

    public static class MessageBrokerHolder {
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }
    private MessageBrokerImpl() {
        eventFutureMap = new ConcurrentHashMap<>();
        subscriberRegisterMap = new ConcurrentHashMap<>();
        MessageSupPubMap = new ConcurrentHashMap<>();
    }
    public static MessageBrokerImpl getInstance() {
        return MessageBrokerHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        if (MessageSupPubMap.get(type) == null) {// if topic doesnt exist then create it.
            MessageSupPubMap.putIfAbsent(type, new LinkedBlockingQueue<Subscriber>());
        }
        MessageSupPubMap.get(type).add(m);// add sub m to topic.
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        if (MessageSupPubMap.get(type) == null) {// if topic doesnt exist then create it.
            MessageSupPubMap.putIfAbsent(type, new LinkedBlockingQueue<>());
        }
        MessageSupPubMap.get(type).add(m); // add sub m to topic
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        eventFutureMap.get(e).resolve(result);// simply resolve the future object.
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        LinkedBlockingQueue<Subscriber> queueSubs = MessageSupPubMap.get(b.getClass());
        if (queueSubs == null)
            return;
        for (Subscriber sub : queueSubs) {
            BlockingQueue<Message> registerQ = subscriberRegisterMap.get(sub);
                if (registerQ != null)
                registerQ.add(b);
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        BlockingQueue<Subscriber> queueSubs = MessageSupPubMap.get(e.getClass());
        Future<T> send = new Future<>();
        synchronized (queueSubs) {
            if (queueSubs == null)
                return null;
            eventFutureMap.put(e, send);
            Subscriber sub = queueSubs.poll();
            if (sub != null) {
                BlockingQueue<Message> Qregister = subscriberRegisterMap.get(sub);
                if (Qregister != null)
                Qregister.add(e);
                queueSubs.add(sub);
            }
            return send;
        }
    }

    @Override
    public void register(Subscriber m) {
        if (!subscriberRegisterMap.contains(m)) {
            subscriberRegisterMap.putIfAbsent(m, new LinkedBlockingQueue<>());
        }
    }

    @Override
    public void unregister(Subscriber m) {
        // remove m from the subscriberRegisterMap
        if (subscriberRegisterMap.remove(m) != null) {
            // if m was in the subscriberRegisterMap  then remove it from each topic he was register to.
            for (Map.Entry<Class<? extends Message>, LinkedBlockingQueue<Subscriber>> iter : MessageSupPubMap.entrySet()) {
                LinkedBlockingQueue<Subscriber> subQ = iter.getValue();
                if (subQ.contains(m)) {
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

