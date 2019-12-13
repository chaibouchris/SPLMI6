package bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        // TODO Implement this
    }

    public static JsonObject Read (String file) throws FileNotFoundException {
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
}

