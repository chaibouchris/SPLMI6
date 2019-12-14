package bgu.spl.mics;

public class testsub extends Subscriber {

    testCallback2 call1;
    testCallback call2;



    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public testsub(String name , testCallback call1 , testCallback2 call2) {
        super(name);
       this.call2 = call1;
       this.call1 = call2;

    }

    @Override
    protected void initialize() {

    }
}
