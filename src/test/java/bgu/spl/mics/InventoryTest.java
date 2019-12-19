package bgu.spl.mics;

import bgu.spl.mics.application.MI6Runner;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory Invi;
    private String[] Gadgets = {"SatSolver","Pizza","Burekas","Tinder","Lupital"};

    @BeforeEach
    public void setUp(){
        try {
            Invi = Inventory.getInstance();
            Invi.load(Gadgets);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void GetItemsTest(){
        assertTrue(Invi.getItem("SatSolver"));
        assertFalse(Invi.getItem("SatSolver"));
        assertFalse(Invi.getItem("Black Zucchini"));
    }

    @Test
    public void PrintToFileTest() {
        try {
            Invi.printToFile("inventoryOutputFile.json");
            JsonObject read = MI6Runner.Read("inventoryOutputFile.json");
            JsonArray Harry = read.getAsJsonArray("inventory");
            List<String> invi = new ArrayList<>();
            for (int i = 0; i < Harry.size(); i++) {
                invi.add(Harry.get(i).toString());
            }
            assertEquals(5,Harry.size());
            while (!invi.isEmpty()){
                assertTrue(Invi.getItem(invi.get(0)));
                invi.remove(0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
