package bgu.spl.mics.application.myClasses;

public class GadgetAvialableResult {

    private int time;
    private Boolean result;

    public GadgetAvialableResult(Boolean r, int time){
        this.time = time;
        result = r;
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
