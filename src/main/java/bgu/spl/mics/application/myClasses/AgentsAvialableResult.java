package bgu.spl.mics.application.myClasses;

import java.util.List;

public class AgentsAvialableResult {

    private  int MoneyPennyID;
    private Boolean result;
    private List<String> agentsNames;

    public AgentsAvialableResult(int MP, Boolean r, List<String> aN){
        this.MoneyPennyID = MP;
        this.result = r;
        this.agentsNames = aN;
    }

    public Boolean getResult(){
        return result;
    }

}
