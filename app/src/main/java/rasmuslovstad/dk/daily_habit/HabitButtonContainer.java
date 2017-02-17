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

/**
 * Created by rasmuslovstad on 2/14/17.
 */

public class HabitButtonContainer implements Serializable{
    public Habit habit;
    public View view;
    public Button button;
    public HabitButtonContainer(Habit habit, View view) {
        this.habit = habit;
        this.view = view;
    }

    Habit getHabit() {
        return habit;
    }
    View getView() {
        return view;
    }

    Button getButton(Context context) {
        habit.updateButton(view);
        button = habit.getButton();
        return button;
    }

    LinearLayout getLayout(Context context) { //Returns a linear layout (Gets used whenever you want a layout that already exists)
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.habit,null,false);

        TextView tvHabit = (TextView) linearLayout.findViewById(R.id.habitText);
        linearLayout.removeView(tvHabit);
        Button btHabit = (Button) linearLayout.findViewById(R.id.habitButton);
        linearLayout.removeView(btHabit);
        //Log.d("HabitButtonContainer",view.getParent().toString());
        if ((ViewGroup)view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        linearLayout.addView(view,0);

        return linearLayout;
    }

    public void prepareForRemoval(Context context) {
        habit.prepareForRemoval(context, view);
    }

    public void delete() {
        habit.delete();
    }
}
