package dktrotti.transitr;

/**
 * Tracks current buses.
 *
 * Created by Dominic on 16-02-07.
 */
public class BusManager {
    private static BusManager ourInstance = new BusManager();

    public static BusManager getInstance() {
        return ourInstance;
    }

    private BusManager() {
    }
}
