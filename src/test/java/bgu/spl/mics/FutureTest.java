package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<String> bejerano;
    private Future<String> bejerano2;
    private Future<String> bejerano3;
    private Future<String> bejerano4;

    @BeforeEach
    public void setUp(){
        bejerano = new Future<>();
        bejerano2 = new Future<>();
        bejerano3 = new Future<>();
        bejerano4 = new Future<>();

    }

    @Test
    public void testIsDone(){
        assertFalse(bejerano.isDone());
        bejerano.resolve("Im professional");
        assertTrue(bejerano.isDone());

        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }

    public void testResolve(){
        assertFalse(bejerano2.isDone());
        bejerano2.resolve("why not try?");
        assertEquals("why not try?",bejerano2.get(),"resolve is stupid");
        assertTrue(bejerano2.isDone());
    }

    public void testTimeGet() throws InterruptedException {
        Thread get = new Thread(()->{
           assertEquals("bigbigbig",bejerano3.get(10, TimeUnit.SECONDS),"resolve to slow");
           assertNull( bejerano4.get(10 ,TimeUnit.SECONDS),"didnt wait to get");


        });
        Thread resolve = new Thread(()->{
          bejerano3.resolve("bigbigbig");
            try {
                get.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bejerano4.resolve("brazil");
        });
        Thread END = new Thread(()->{
           assertTrue(bejerano3.isDone(),"resolve bejerano3 aint working");
            assertTrue(bejerano4.isDone(),"resolve bejerano4 anit workin");
            assertEquals("brazil",bejerano4.get(),"resolve suck");
        });

        get.start();
        resolve.start();
        resolve.join();
        END.start();

    }
}
