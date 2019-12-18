package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.MissionInfo;


public class MissionReceivedEvent {

    private MissionInfo mission;

    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.mission = missionInfo;
    }

    public MissionInfo getMissionInfo() {
        return mission;
    }
}
