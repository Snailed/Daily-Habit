package rasmuslovstad.dk.daily_habit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    ArrayList habitlist = new ArrayList<Habit>();
    RelativeLayout backgroundLayout;
    LinearLayout habit;
    LayoutInflater layoutInflater;
    Habit pushups;
    Habit weightlifting;
    Button btTilfoej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundLayout = (RelativeLayout) findViewById(R.id.activity_main);
        habitlist.add(new Habit(70, "Pushups",this));
        //Log.d("Test",habitlist.get(habitlist.size()-1).getLayout());
        //backgroundLayout.addView();
        btTilfoej = (Button) findViewById(R.id.btNewHabit);

        weightlifting = new Habit(30, "Weight Lifting",this);
        backgroundLayout.addView(weightlifting.getLayout());


    }

    public void pressComplete(View view) {
        Log.d("MainActivity ", "Pling!");
        if (view instanceof Button) {
            Habit habit = Habit.getHabitFromView(view);
            //Log.d("Habit:", "Habit "+habit);
            if (habit.habitState()) {
                habit.undoCompleteObjective();
            } else {
                habit.completeObjective();
            }
        }
    }

    public void pressCreateNewHabit(View view) {
        Intent intent = new Intent(this,CreateHabit.class);
        startActivity(intent);

    }
}
