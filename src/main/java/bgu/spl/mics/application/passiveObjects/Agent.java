package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
    private String SerialNumber;
    private String Name;
    private AtomicBoolean isAvailable;
    private Semaphore lock1;


    public Agent(String name, String serialNumber) {
        this.Name = name;
        this.SerialNumber = serialNumber;
        isAvailable = new AtomicBoolean(true);
        lock1 = new Semaphore(1, true);
    }

    /**
     * Sets the serial number of an agent.
     */
    public void setSerialNumber(String serialNumber) {
        this.SerialNumber = serialNumber;
    }

    /**
     * Retrieves the serial number of an agent.
     * <p>
     *
     * @return The serial number of an agent.
     */
    public String getSerialNumber() {
        return SerialNumber;
    }

    /**
     * Sets the name of the agent.
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * Retrieves the name of the agent.
     * <p>
     *
     * @return the name of the agent.
     */
    public String getName() {
        return Name;
    }

    /**
     * Retrieves if the agent is available.
     * <p>
     *
     * @return if the agent is available.
     */
    public boolean isAvailable() {
        return isAvailable.get();
    }

    /**
     * Acquires an agent.
     */
    public void acquire() {
        try {
            lock1.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isAvailable.set(false);
    }


    /**
     * Releases an agent.
     */
    public void release() {
        if (!isAvailable()) {
            isAvailable.set(true);
        }
        lock1.release();
    }
}
