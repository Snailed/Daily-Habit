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

    //buttons that belong to the habit
    Button btHabit;
    TextView tvHabit;

    //List of buttons and habits. Every habit has a view on the same index in each list.
    static ArrayList<View> buttons = new ArrayList<>();
    static ArrayList<Habit> habits = new ArrayList<>();

    transient Datamanager datamanager = Datamanager.getInstance();

    Habit(int repetitions, String titel) {
        this.titel = titel;
        this.repetitions = repetitions;


    }



    Habit(int repetitions, String titel, boolean state) {
        this.titel = titel;
        this.repetitions = repetitions;
        this.completedObjective = state;
        Log.d("Habit", "Har lavet en habit med titlen "+this.titel+", repetitionerne "+this.repetitions+" og staten "+this.completedObjective);


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
        buttons.add(btHabit);
        Log.d("Habit", "Tilføjede "+btHabit+" til buttons som nu har størrelsen "+ buttons.size());
        //linearLayout.setY(150*++numberOfHabits);

        if (!habits.contains(this)) {
            habits.add(this);

        }
        if (datamanager == null) datamanager = Datamanager.getInstance();




        return linearLayout;
    }

    void completeObjective(View view) {
        updateButton(view);
        completedObjective = true;
        btHabit.setText("");
        setMargins(btHabit,0,0,0,10);
        btHabit.setBackgroundResource(R.drawable.completetaskbuttonpressed);


    }

    void undoCompleteObjective(View view) {
        updateButton(view);
        completedObjective = false;
        if (repetitions != 0)btHabit.setText(""+ repetitions);
        else btHabit.setText("");
        setMargins(btHabit,0,0,0,10);
        btHabit.setBackgroundResource(R.drawable.completetaskbutton);

    }

    void updateHabit(Context context, View view) {
        updateButton(view);
        if (completedObjective) {
            btHabit.setText("");
            setMargins(btHabit,0,0,0,10);
            btHabit.setBackgroundResource(R.drawable.completetaskbuttonpressed);
        } else {
            if (repetitions != 0)btHabit.setText(""+ repetitions);
            else btHabit.setText("");
            setMargins(btHabit,0,0,0,10);
            btHabit.setBackgroundResource(R.drawable.completetaskbutton);
        }
    }


    void prepareForRemoval(Context context, View view) {
        updateButton(view);
        if (btHabit == null) {
            btHabit =(Button) getViewFromHabit(this);
            Log.d("Habit", "Denne habit: "+this+" har intet view");
        }
        btHabit.setText("");
        btHabit.setBackgroundResource(R.drawable.ic_clear_black_24dp_black);
    }

    void delete() {
        buttons.remove(btHabit);
        habits.remove(this);
    }

    boolean habitState() {
        return completedObjective;
    }

    Button getButton() {

        return btHabit;
    }

    static Habit getHabitFromView(View view) {
        if (buttons.contains(view)) {
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i)==view) {
                    return habits.get(i);
                }
            }
            Log.d("Habit", "There is no corresponding habit to the view "+view);
        } else Log.d("Habit", "Exception! "+view+" was not found in "+ Arrays.toString(buttons.toArray())+"  Size:  "+ buttons.size()+ " Number of habits: "+ numberOfHabits);
        return null;
    }

    static View getViewFromHabit(Habit habit) {
        if (habits.contains(habit)) {
            for (int i = 0; i < habits.size(); i++) {
                if (habits.get(i)==habit) {
                    return buttons.get(i);
                }
            }
            Log.d("Habit", "There is no corresponding view to the habit "+habit);
        } else Log.d("Habit", "Exception! "+habit+" was not found in "+ Arrays.toString(habits.toArray())+"  Size:  "+habits.size()+ " Number of habits: "+ numberOfHabits);
        return null;
    }

    static void addToHabitsAndButtons(Habit habit) {

    }
    public void  updateButton(View view) {
        btHabit = (Button) view.findViewById(R.id.habitButton);
    }

    public int getRepetitions() {
        return repetitions;
    }

    public String getTitel() {
        return titel;
    }

    public boolean isCompletedObjective() {
        return completedObjective;
    }

    private static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }


}
