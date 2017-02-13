package rasmuslovstad.dk.daily_habit;

/**
 * Created by rasmuslovstad on 2/13/17.
 */
public class Datamanager {
    private static Datamanager ourInstance = new Datamanager();

    public static Datamanager getInstance() {
        return ourInstance;
    }

    private Datamanager() {
    }
}
