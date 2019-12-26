package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;


public class MissionReceivedEvent implements Event<Boolean> {

    private MissionInfo mission;

    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.mission = missionInfo;
    }

    public MissionInfo getMissionInfo() {
        return mission;
    }

    public int getDuration(){
        return this.getMissionInfo().getDuration();
    }

    public int getEndTime(){
        return this.getMissionInfo().getTimeExpired();
    }

    public List<String> getSerials(){
        return this.getMissionInfo().getSerialAgentsNumbers();
    }

    public String getGadget(){
        return this.getMissionInfo().getGadget();
    }

    public int getTimeIssued(){
        return this.getMissionInfo().getTimeIssued();
    }
}
