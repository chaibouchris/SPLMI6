package bgu.spl.mics.application.passiveObjects;

public class GadgetAvialableResult {

    private int time;
    private Boolean result;

    public GadgetAvialableResult(){
        time = -1;
        result = false;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int toSet){
        this.time = toSet;
    }

    public Boolean getResult(){
        return result;
    }

    public void setResult(Boolean r){
        this.result = r;
    }
}
