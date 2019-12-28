package bgu.spl.mics.application;

import bgu.spl.mics.application.others.LoadLatch;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
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
        //String path = args[0];
        String path = "tamirJson1.json";
        List<Thread> fredList = new ArrayList<>();
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
        loadM(services, threadList);
        LoadMoneypenny(services, threadList);
        LoadQ(threadList);
        LoadIntelligence(services, threadList);
        Thread timeService = LoadTimeService(services);


        LoadLatch latch = new LoadLatch(threadList, timeService);
        latch.start();

        for (Thread freddi : threadList){
            try {
                freddi.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Inventory.getInstance().printToFile("inventory.json");
        Diary.getInstance().printToFile("diary.json");
    }

    private static Thread LoadTimeService(JsonObject services) {
        int time = services.get("time").getAsInt();
        TimeService tiesto = new TimeService(time);
        Thread timeService = new Thread(tiesto);
        timeService.setName("TimeService");
        return timeService;
    }

    private static void LoadIntelligence(JsonObject services, List<Thread> threadList) {
        JsonArray intelligence = services.getAsJsonArray("intelligence");
        for (int i = 0; i < intelligence.size(); i++){
            List<MissionInfo> theList = new ArrayList<MissionInfo>();
            JsonObject JJ = intelligence.get(i).getAsJsonObject();
            JsonArray missions = JJ.getAsJsonArray("missions");
            for (int j = 0; j < missions.size(); j++){
                JsonObject misInfo = missions.get(j).getAsJsonObject();
                JsonArray serials = misInfo.getAsJsonArray("serialAgentsNumbers");
                List<String> serialNumbers = new ArrayList<>();
                for (int k = 0; k < serials.size(); k++){
                    String serial = serials.get(k).getAsString();
                    serialNumbers.add(serial);
                }
                int duration = misInfo.get("duration").getAsInt();
                String gadget = misInfo.get("gadget").getAsString();
                String name = misInfo.get("name").getAsString();
                int expired = misInfo.get("timeExpired").getAsInt();
                int issued = misInfo.get("timeIssued").getAsInt();

                MissionInfo missionInfo = new MissionInfo(name, serialNumbers, gadget, issued, expired, duration);
                theList.add(missionInfo);
            }
            Intelligence intel = new Intelligence(i, theList);
            Thread Ekrueger = new Thread(intel);
            Ekrueger.setName("Intelligence "+i);
            threadList.add(Ekrueger);
        }
    }

    private static void LoadQ(List<Thread> threadList) {
        Thread q = new Thread(new Q());
        q.setName("Q");
        threadList.add(q);
    }

    private static void LoadMoneypenny(JsonObject services, List<Thread> threadList) {
        int cuantosMoneypenny = services.get("Moneypenny").getAsInt();
        for (int j = 0; j < cuantosMoneypenny; j++){
            Moneypenny Mp3 = new Moneypenny(j);
            Thread fredi = new Thread(Mp3);
            fredi.setName("Moneypenny "+j);
            threadList.add(fredi);
        }
    }

    private static void loadM(JsonObject services, List<Thread> threadList) {
        int cuantosM = services.get("M").getAsInt();
        for (int i = 0; i < cuantosM; i++){
            M mamasita = new M(i);
            Thread fred = new Thread(mamasita);
            fred.setName("M "+i);
            threadList.add(fred);
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

