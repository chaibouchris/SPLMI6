package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {

    private Squad squad;
    private Agent[] agents;

    @BeforeEach
    public void setUp(){
        try {
            squad = Squad.getInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
