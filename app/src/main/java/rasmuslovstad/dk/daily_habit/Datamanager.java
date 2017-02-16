package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.util.Log;
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
        Habit.habits.add(habit.getHabit());
        Habit.buttons.add(habit.getView().findViewById(R.id.habitButton));

    }

    public void prepareButtonsForRemoval(Context context) {
        for (HabitButtonContainer i :
                habitList) {
            i.prepareForRemoval(context);
            Log.d("Datamanager", "Prepared habit "+i+" for removal");
        }
    }
    public void undoPrepareButtonsForRemoval(Context context) {
        for (HabitButtonContainer i :
                habitList) {
            i.getHabit().updateHabit(context);
        }

    }
    public void removeHabit(HabitButtonContainer habit) {
        habitList.remove(habit);
        habit.delete();
    }

    public HabitButtonContainer getHabitButtonContainer(View view,Context context) {

        for (HabitButtonContainer i :
                habitList) {

            if (i.getButton(context) == view) {
                Log.d("Datamanager", "Returned "+i);
                return i;
            }
        }
        return null;
    }


}
