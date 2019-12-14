package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBrokerImpl jonyboy1;
    testsub bejerano;
    testEvent getEng;
    testBrodcast hey;
    testCallback call1;
    testCallback2 call2;


    @BeforeEach
    public void setUp(){
    jonyboy1 = new MessageBrokerImpl();
    bejerano = new testsub("bejerano",call1,call2);
    getEng = new testEvent();
    hey = new testBrodcast();




    }

    @Test
    // stupid sent an event (wants to get bigger engine) to jony
    // jony relay event to bejerano
    // bejerano callback - (change engine to bigger)

    public void testSenerio1(){
     jonyboy1.subscribeEvent(testEvent.class,bejerano);
     jonyboy1.sendEvent(getEng);
     assertEquals("bigEingen",getEng.getTochange(),"jony is lazy");

    }

    public void testSenerio2(){
        jonyboy1.subscribeEvent(testEvent.class,bejerano);
        jonyboy1.sendBroadcast(hey);
        assertTrue(hey.isgood);


    }
}
