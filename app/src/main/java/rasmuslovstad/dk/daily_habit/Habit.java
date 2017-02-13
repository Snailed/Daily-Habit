package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by rasmuslovstad on 1/4/17.
 */

public class Habit {
    int reps;
    String titel;
    Context context;
    static int habitcounter = 0;
    boolean completedObjective = false;
    Button btHabit;
    static ArrayList<View> views;
    static ArrayList<Habit> habits;
    Habit(int reps, String titel,Context context) {
        this.titel = titel;
        this.reps = reps;
        this.context = context;
        views = Datamanager.getInstance().views;
        habits = Datamanager.getInstance().habits;
    }
    LinearLayout getLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.habit,null,false);
        TextView tvHabit = (TextView) linearLayout.findViewById(R.id.habitText);
        tvHabit.setText(titel);
        tvHabit.setTextSize(25);
        btHabit = (Button) linearLayout.findViewById(R.id.habitButton);
        btHabit.setText(""+reps);
        btHabit.setBackgroundResource(R.drawable.completetaskbutton);
        linearLayout.setY(150*++habitcounter);
        views.add(btHabit);
        Log.d("Habit", "Tilføjede "+btHabit+" til views som nu har størrelsen "+views.size());
        habits.add(this);

        return linearLayout;
    }

    void completeObjective() {
        completedObjective = true;
        btHabit.setText("");
        setMargins(btHabit,0,-20,0,0); //Gør så dette ændrer på Habit-layoutet.
        btHabit.setBackgroundResource(R.drawable.completetaskbuttonpressed);


    }

    void undoCompleteObjective() {
        completedObjective = false;
        btHabit.setText(""+reps);
        setMargins(btHabit,0,0,0,0);
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
            Log.d("Habit modtager", "Hvad sker der? Dette er ikke en habit!"+view);
        } else Log.d("Habit", "Der opstod en exception! "+view+" findes ikke i "+ Arrays.toString(views.toArray())+"  Størrelse:  "+views.size()+ " Habitcounter: "+habitcounter);
        return null;
    }

    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

}
