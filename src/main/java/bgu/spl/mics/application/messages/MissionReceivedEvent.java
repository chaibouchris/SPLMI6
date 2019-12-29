package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

/**
 * MissionReceiveEvent is an Event that Intelligence send to the MessageBroker and than the MessageBroker
 * send to the M's so one of them will handle it.
 * we contain in this event the mission info.
 */
public class MissionReceivedEvent implements Event<Boolean> {

    private MissionInfo mission;

    /**
     * public constructor of this class.
     */
    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.mission = missionInfo;
    }

    /**
     * Retrieves the mission info.
     */
    public MissionInfo getMissionInfo() {
        return mission;
    }

    /**
     * Retrieves the duration if the mission.
     */
    public int getDuration(){
        return this.getMissionInfo().getDuration();
    }

    /**
     * Retrieves the expired time of the mission.
     */
    public int getExpiredTime(){
        return this.getMissionInfo().getTimeExpired();
    }

    /**
     * Retrieves the list of the serials of the agents.
     */
    public List<String> getSerials(){
        return this.getMissionInfo().getSerialAgentsNumbers();
    }

    /**
     * Retrieves the gadgets of the mission.
     */
    public String getGadget(){
        return this.getMissionInfo().getGadget();
    }

    /**
     * Retrieves the time issued of the mission.
     */
    public int getTimeIssued(){
        return this.getMissionInfo().getTimeIssued();
    }
}
