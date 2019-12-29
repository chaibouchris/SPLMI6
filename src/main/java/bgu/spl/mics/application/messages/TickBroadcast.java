package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * Broadcast messages represents a global announcement in the system. Each
 * Subscriber can register to the type of broadcast messages it is interested to receive. The
 * MessageBroker sends the broadcast messages that are passed to it to all the Subscribers who
 * are registered to the topic (this is in contrast to events - those are sent to only one of the
 * relevant Subscribers).
 * TimeService send TickBroadcast every tick so the subscribers will update the tick they contain.
 */
public class TickBroadcast implements Broadcast {

    private int tick;

    /**
     * public constructor of this class.
     */
    public TickBroadcast(int tick) {
        this.tick = tick;
    }

    /**
     * Retrieves the tick of the broadcast.
     */
    public int getTick() {
        return tick;
    }

}
