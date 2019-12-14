//package bgu.spl.mics;
package test.java.bgu.spl.mics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.bgu.spl.mics.application.passiveObjects.Inventory;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
public class InventoryTest {
    Inventory ronen;
    @BeforeEach
    public void setUp(){
    ronen = new Inventory();
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
    public void testGetItem(){
        String [] items =  {"gun", "hammer" ,"formulas"};
        ronen.load(items);
        for (String i:items) {
            assertTrue(ronen.getItem(i));
            assertFalse(ronen.getItem(i));
        }
    }
    public void testPrintToFile(){
        String toPrint = "printTHIStoJson";
        String filePathString = "*.file name.json";
        inv1.printToFile(str);
        File f = new File(filePathString);
        boolean isExist = false;
        if(f.exists() && !f.isDirectory()) {
            isExist = true;
        }
        assertTrue(isExist);

}
