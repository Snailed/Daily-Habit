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
    //request codes
    static final int RECIEVE_HABIT = 1;

    RelativeLayout backgroundLayout;
    Habit pushups;
    Habit weightlifting;
    Button btTilfoej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundLayout = (RelativeLayout) findViewById(R.id.activity_main);
        btTilfoej = (Button) findViewById(R.id.btNewHabit);
        pushups = new Habit(70, "Pushups");
        weightlifting = new Habit(30, "Weight Lifting");
        backgroundLayout.addView(pushups.getLayout(this));
        backgroundLayout.addView(weightlifting.getLayout(this));


    }

    public void pressComplete(View view) {
        //Log.d("MainActivity ", "A button was pushed!");
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
        startActivityForResult(intent, RECIEVE_HABIT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) Log.d("MainActivity", "Intenten som er modtaget fra CreateHabit er null... "+data);

        if (requestCode == RECIEVE_HABIT) {

            if (resultCode == RESULT_OK) {
                backgroundLayout.addView(((Habit) data.getExtras().getSerializable("Habit")).getLayout(this));
            }

        }
    }
}
