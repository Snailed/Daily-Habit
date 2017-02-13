package rasmuslovstad.dk.daily_habit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CreateHabit extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
    }

    public void pressConfirmCreateNewHabit(View v) {
        Log.d("MainActivity","Plingo!");

    }
}
