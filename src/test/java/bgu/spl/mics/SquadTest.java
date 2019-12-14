package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;
//import Squad;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest<> {
    private Squad ;

    @BeforeEach
    public void setUp(){
        Squad = new Squad();
    }

    @Test
    public void getAgents(){
        Agent ronen = new Agent ("001", "ronen", true );
        Agent chai = new Agent ("002", "ronen", true );
        Agent sahar = new Agent ("003", "ronen", true );

        Agent [] agents = new Agent[5];

        Squad.load()
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
