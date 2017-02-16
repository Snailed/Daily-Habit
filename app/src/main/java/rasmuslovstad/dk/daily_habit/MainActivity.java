package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{
    //request codes
    static final int RECIEVE_HABIT = 1;
    public static final boolean DEVELOPER_MODE = true;


    RelativeLayout backgroundLayout;
    LinearLayout habitlistLayout;
    Habit pushups;
    Habit weightlifting;
    FloatingActionButton btTilfoej;
    Datamanager datamanager;
    static boolean preparedForRemoval = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datamanager = Datamanager.getInstance();
        setContentView(R.layout.activity_main);
        backgroundLayout = (RelativeLayout) findViewById(R.id.activity_main);
        habitlistLayout = (LinearLayout) findViewById(R.id.habitlist);
        btTilfoej = (FloatingActionButton) findViewById(R.id.btNewHabit);

        if (DEVELOPER_MODE) addDeveloperHabits();
        addHabits();

    }

    public void pressComplete(View view) {
        //Log.d("MainActivity ", "A button was pushed!");

        if (!preparedForRemoval) { //When the habits are pressed, they should check or uncheck. If preparedForRemoval is true, the habit should disappear
            if (view instanceof Button) {

                Habit habit = datamanager.getHabitButtonContainer(view,this).getHabit();
                //Log.d("Habit:", "Habit "+habit);
                if (habit == null) Log.d("MainActivity", "Halløj! Denne view har ingen habit :( " + view + " Liste: " + Habit.habits);
                if (habit.habitState()) {
                    habit.undoCompleteObjective();
                } else {
                    habit.completeObjective(view);
                }
            }

        } else {
            datamanager.getInstance().removeHabit(datamanager.getHabitButtonContainer(view,this));
            recreate();
        }

    }

    public void pressCreateNewHabit(View view) {
        datamanager.undoPrepareButtonsForRemoval(this);
        preparedForRemoval = false;
        Intent intent = new Intent(this,CreateHabit.class);
        startActivityForResult(intent, RECIEVE_HABIT);


    }

    public void pressRemoveHabit(View view) {
        if (!preparedForRemoval) {
            datamanager.prepareButtonsForRemoval(this);
            preparedForRemoval = true;
        } else {
            datamanager.undoPrepareButtonsForRemoval(this);
            preparedForRemoval = false;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) Log.d("MainActivity", "Intenten som er modtaget fra CreateHabit er null... "+data);

        if (requestCode == RECIEVE_HABIT) {

            if (resultCode == RESULT_OK) {
                HabitButtonContainer habitButtonContainer = new HabitButtonContainer((Habit) data.getExtras().getSerializable("Habit"),((Habit) data.getExtras().getSerializable("Habit")).createLayout(this));
                datamanager.addHabit(habitButtonContainer);
                //habitlistLayout.addView(((Habit) data.getExtras().getSerializable("Habit")).createLayout(this));
                habitlistLayout.removeAllViews();
                addHabits();


            }

        }
    }

    private void addHabits() {
        for (LinearLayout i: datamanager.getAllLayout(this)) {

            habitlistLayout.addView(i);
        }

    }

    private void addDeveloperHabits() {
        if (datamanager.firstTimeRun) {
            Log.d("MainActivity", "Added first time developer habits");
            pushups = new Habit(70, "Pushups");
            weightlifting = new Habit(30, "Weight Lifting");
            datamanager.addHabit(new HabitButtonContainer(pushups,pushups.createLayout(this)));
            datamanager.addHabit(new HabitButtonContainer(weightlifting,weightlifting.createLayout(this)));
            datamanager.firstTimeRun = false;
        }
    }

}
