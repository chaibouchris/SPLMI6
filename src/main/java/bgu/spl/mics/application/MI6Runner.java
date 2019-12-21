package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import com.google.gson.*;

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
        JsonObject derulo = null;
        try {
            derulo = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Subscriber> subs = loadSubscribers(derulo);
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

    public static List<Subscriber> loadSubscribers(JsonObject JJ){
        List<Subscriber> thelist = new ArrayList<>();
        return thelist;
    }
}

