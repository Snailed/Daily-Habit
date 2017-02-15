package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rasmuslovstad on 1/4/17.
 */

public class Habit implements Serializable{

    int repetitions;
    public String titel;
    static int numberOfHabits = 0;
    boolean completedObjective = false;

    //views that belong to the habit
    Button btHabit;
    TextView tvHabit;

    //List of views and habits. Every habit has a view on the same index in each list.
    static ArrayList<View> views = new ArrayList<>();
    static ArrayList<Habit> habits = new ArrayList<>();

    transient Datamanager datamanager = Datamanager.getInstance();

    Habit(int repetitions, String titel) {
        this.titel = titel;
        this.repetitions = repetitions;


    }
    LinearLayout createLayout(Context context) { //Gets called when you want a new layout


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.habit,null,false);

        tvHabit = (TextView) linearLayout.findViewById(R.id.habitText);
        tvHabit.setText(titel);
        tvHabit.setTextSize(25);


        btHabit = (Button) linearLayout.findViewById(R.id.habitButton);
        if (repetitions != 0)btHabit.setText(""+ repetitions);
        else btHabit.setText("");
        btHabit.setBackgroundResource(R.drawable.completetaskbutton);
        setMargins(btHabit,0,0,0,10);
        views.add(btHabit);
        Log.d("Habit", "Tilføjede "+btHabit+" til views som nu har størrelsen "+views.size());
        //linearLayout.setY(150*++numberOfHabits);

        if (!habits.contains(this)) {
            habits.add(this);

        }
        if (datamanager == null) datamanager = Datamanager.getInstance();




        return linearLayout;
    }

    void completeObjective() {
        completedObjective = true;
        btHabit.setText("");
        setMargins(btHabit,0,0,0,10);
        btHabit.setBackgroundResource(R.drawable.completetaskbuttonpressed);


    }

    void undoCompleteObjective() {
        completedObjective = false;
        if (repetitions != 0)btHabit.setText(""+ repetitions);
        else btHabit.setText("");
        setMargins(btHabit,0,0,0,10);
        btHabit.setBackgroundResource(R.drawable.completetaskbutton);

    }

    boolean habitState() {
        return completedObjective;
    }

    static Habit getHabitFromView(View view) {
        if (views.contains(view)) {
            for (int i = 0; i < views.size(); i++) {
                if (views.get(i)==view) {
                    return habits.get(i);
                }
            }
            Log.d("Habit", "There is no corresponding habit to the view "+view);
        } else Log.d("Habit", "Exception! "+view+" was not found in "+ Arrays.toString(views.toArray())+"  Size:  "+views.size()+ " Number of habits: "+ numberOfHabits);
        return null;
    }

    static View getViewFromHabit(Habit habit) {
        if (habits.contains(habit)) {
            for (int i = 0; i < habits.size(); i++) {
                if (habits.get(i)==habit) {
                    return views.get(i);
                }
            }
            Log.d("Habit", "There is no corresponding view to the habit "+habit);
        } else Log.d("Habit", "Exception! "+habit+" was not found in "+ Arrays.toString(habits.toArray())+"  Size:  "+habits.size()+ " Number of habits: "+ numberOfHabits);
        return null;
    }

    private static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

}
