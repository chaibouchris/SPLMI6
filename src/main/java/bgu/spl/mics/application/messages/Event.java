package bgu.spl.mics.application.messages;

public class Event implements bgu.spl.mics.Event {

    private String senderName;
    private String kind;

    public Event(String senderName, String kind) {
        this.kind = kind;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}


