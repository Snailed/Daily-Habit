package rasmuslovstad.dk.daily_habit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class CreateHabit extends AppCompatActivity {
    EditText title;
    EditText reps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_create_habit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.etTitle);
        reps = (EditText) findViewById(R.id.etReps);
    }

    public void pressConfirmCreateNewHabit(View v) {
        if (title.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "You have to give your habit a title", Toast.LENGTH_SHORT).show();
        } else {
            int repetitioner = 0;
            if (!reps.getText().toString().equals("")) {
                repetitioner = Integer.parseInt(reps.getText().toString());
            }

            Habit resultHabit = new Habit(repetitioner, title.getText().toString());
            Intent result = new Intent();
            result.putExtra("Habit", resultHabit);
            setResult(RESULT_OK, result);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
