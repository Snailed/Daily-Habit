package rasmuslovstad.dk.daily_habit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateHabit extends AppCompatActivity {
    EditText title;
    EditText reps;
    EditText unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        title = (EditText) findViewById(R.id.etTitle);
        reps = (EditText) findViewById(R.id.etReps);
        unit = (EditText) findViewById(R.id.etUnit);
    }

    public void pressConfirmCreateNewHabit(View v) {

        int repetitioner = 0;
        if (!reps.getText().toString().equals("")) {
            repetitioner = Integer.parseInt(reps.getText().toString());
        }

        Habit resultHabit = new Habit(repetitioner,title.getText().toString());
        Intent result = new Intent();
        result.putExtra("Habit",resultHabit);
        setResult(RESULT_OK, result);
        finish();
    }
}
