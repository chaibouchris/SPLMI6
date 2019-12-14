package bgu.spl.mics;

public class testCallback implements Callback {

    public void call(testEvent c) {
        c.setTochange("bigEingen");

    }

    @Override
    public void call(Object c) {

    }
}
