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
    Habit habit;
    View view;
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

}
