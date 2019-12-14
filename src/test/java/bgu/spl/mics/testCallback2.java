package bgu.spl.mics;

public class testCallback2 implements Callback{


    public void call(testBrodcast b) {
        b.isgood=true;


    }

    @Override
    public void call(Object c) {

    }
}
