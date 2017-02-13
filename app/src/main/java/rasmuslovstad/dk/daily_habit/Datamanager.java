package rasmuslovstad.dk.daily_habit;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by rasmuslovstad on 2/13/17.
 */
public class Datamanager {
    private static Datamanager ourInstance = new Datamanager();
    public ArrayList<View> views = new ArrayList<>();
    public ArrayList<Habit> habits = new ArrayList<>();

    public static Datamanager getInstance() {
        return ourInstance;
    }

    private Datamanager() {
    }
}
