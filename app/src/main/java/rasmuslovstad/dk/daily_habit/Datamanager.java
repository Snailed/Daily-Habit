package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rasmuslovstad on 2/13/17.
 */
public class Datamanager {
    private static Datamanager ourInstance = new Datamanager();
    public ArrayList<HabitButtonContainer> habitList = new ArrayList<>();
    boolean firstTimeRun = true;

    public static Datamanager getInstance() {
        return ourInstance;
    }

    private Datamanager() {
    }

    public ArrayList<LinearLayout> getAllLayout(Context context) {
        ArrayList<LinearLayout> output = new ArrayList<>();
        for (HabitButtonContainer i : habitList) {
            output.add(i.getLayout(context));
        }
        return output;
    }
    public void addHabit(HabitButtonContainer habit) {
        habitList.add(habit);
    }

}
