package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    public static void main(String[] args) {
        String path = args[0];
        List<Thread> fredList = new ArrayList<>();
        fredList.add(Thread.currentThread());
        try {
            BufferedReader Yoram = new BufferedReader(new FileReader(path));
            Gson Goku = new Gson();
            JsonObject Derulo = Goku.fromJson(Yoram, JsonObject.class);
            LetTheShowtimeBegin(Derulo, fredList);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
    }

    private static void LetTheShowtimeBegin(JsonObject JJ, List<Thread> threadList){
        JsonArray inventory = JJ.getAsJsonArray("inventory");
        JsonArray squad = JJ.getAsJsonArray("squad");
        JsonObject services = JJ.getAsJsonObject("services");
        Inventory.getInstance().load(inventoryToString(inventory));
        Squad.getInstance().load(agentToArray(squad));
        showMustGoOn(services, threadList);
    }

    private static void showMustGoOn(JsonObject services, List<Thread> threadList) {
        int cuantosM = services.get("M").getAsInt();
        for (int i = 0; i < cuantosM; i++){
            M mamasita = new M(i);
            Thread fred = new Thread(mamasita);
            threadList.add(fred);
            fred.start();
        }

        int cuantosMoneypenny = services.get("Moneypenny").getAsInt();
        for (int j = 0; j < cuantosMoneypenny; j++){
            Moneypenny Mp3 = new Moneypenny(j);
            Thread fredi = new Thread(Mp3);
            threadList.add(fredi);
            fredi.start();
        }

        Thread q = new Thread(new Q());
        threadList.add(q);
        q.start();

        JsonArray intelligence = services.getAsJsonArray("intelligence");
        List<MissionInfo> MI = new ArrayList<MissionInfo>();
        for (int i = 0; i < intelligence.size(); i++){
            JsonObject
        }
    }


    public static JsonObject Read (String file) {
        Gson park = new GsonBuilder().setPrettyPrinting().create();
        JsonObject Derulo = null;
        try (Reader reader = new FileReader(file)){
            JsonElement Kid = park.fromJson(reader,JsonElement.class);
            Derulo = Kid.getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Derulo;
    }


    public static String[] inventoryToString(JsonArray inventory){
        String[] gadgets = new String[inventory.size()];
        for (int i = 0; i<inventory.size();i++){
            gadgets[i] = inventory.get(i).getAsString();
        }
        return gadgets;
    }

    public static Agent[] agentToArray (JsonArray squad){
        Agent[] agents = new Agent[squad.size()];
        for (int i = 0; i<squad.size();i++){
            JsonObject agent = squad.get(i).getAsJsonObject();
            for (int j = 0; j<agent.size();j++){
                String name = agent.get("name").getAsString();
                String serialNumber = agent.get("serialNumber").getAsString();
                Agent a = new Agent (name,serialNumber);
                agents[i] = a;
            }
        }
        return agents;
    }
}

