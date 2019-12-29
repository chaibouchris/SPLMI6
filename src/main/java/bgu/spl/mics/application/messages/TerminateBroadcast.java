package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * Broadcast messages represents a global announcement in the system. Each
 * Subscriber can register to the type of broadcast messages it is interested to receive. The
 * MessageBroker sends the broadcast messages that are passed to it to all the Subscribers who
 * are registered to the topic (this is in contrast to events - those are sent to only one of the
 * relevant Subscribers).
 * TerminateBroadcast is a broadcast that sent when the TimeService finish his ticks and send a
 * broadcast that will make the subscriber make terminate and than unregister.
 */
public class TerminateBroadcast implements Broadcast {
}
