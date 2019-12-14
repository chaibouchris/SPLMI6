package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    private Squad squad;
    private Agent[] agents;
    private List<String> Serials;

    @BeforeEach
    public void setUp(){
        try {
            squad = Squad.getInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        agents = new Agent[4];
        for (int i = 0; i < 4; i++)
            agents[i] = new Agent();
        agents[0].setName("Coco Loco");
        agents[1].setName("BB Netanyahu");
        agents[2].setName("Avi Asulin");
        agents[3].setName("Dudu Farook");
        agents[0].setSerialNumber("000");
        agents[1].setSerialNumber("007");
        agents[2].setSerialNumber("010");
        agents[3].setSerialNumber("101");
        squad.load(agents);
        Serials = new ArrayList<>();
    }

    @Test
    public void LoadTest(){
        Serials.add("000");
        Serials.add("007");
        Serials.add("010");
        Serials.add("101");
        List<String> AgentsNames = squad.getAgentsNames(Serials);
        assertTrue(AgentsNames.contains("Coco Loco"));
        assertTrue(AgentsNames.contains("BB Netanyahu"));
        assertTrue(AgentsNames.contains("Avi Asulin"));
        assertTrue(AgentsNames.contains("Dudu Farook"));
    }

    @Test
    public void ReleaseAgentsTest(){
        Serials.add("007");
        Serials.add("101");
        squad.releaseAgents(Serials);
        List<String> AgentsName = squad.getAgentsNames(Serials);
        assertFalse(AgentsName.contains("BB Netanyahu"));
        assertFalse(AgentsName.contains("Dudu Farook"));
    }

    @Test
    public void GetAgentsTest(){
        Serials.add("006");
        assertFalse(squad.getAgents(Serials));
        Serials.remove("006");
        Serials.add("000");
        Serials.add("010");
        assertTrue(squad.getAgents(Serials));
    }

    @Test
    public void GetAgentsNamesTest(){
        Serials.add("000");
        Serials.add("101");
        List<String> names = squad.getAgentsNames(Serials);
        assertTrue(names.contains("Coco Loco"));
        assertTrue(names.contains("Dudu Farook"));
        assertEquals(2,names.size());
    }
}
