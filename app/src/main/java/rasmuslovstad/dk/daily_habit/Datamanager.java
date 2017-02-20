package rasmuslovstad.dk.daily_habit;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by rasmuslovstad on 2/13/17.
 */
public class Datamanager {
    private static Datamanager ourInstance = new Datamanager();
    public ArrayList<HabitButtonContainer> habitList = new ArrayList<>();
    boolean firstTimeRun = true;
    MainActivity context;
    boolean isWritingToDatabase = false;
    boolean firstDataChange = true;
    int isWritingCounter = 0;
    public static Datamanager getInstance() {
        return ourInstance;
    }

    private Datamanager() {
    }

    public void setContext(MainActivity context) {
        this.context = context;
    }


    public ArrayList<LinearLayout> getAllLayout(Context context) {
        ArrayList<LinearLayout> output = new ArrayList<>();
        for (HabitButtonContainer i : habitList) {
            output.add(i.getLayout(context));
        }
        return output;
    }
    public void addHabit(HabitButtonContainer habit) {
        habitList.add(habit);
        Habit.habits.add(habit.getHabit());
        Habit.buttons.add(habit.getView().findViewById(R.id.habitButton));
        Log.d("Datamanager", "Tilføjet habitten "+habit.getHabit().getTitel()+" Nu er habitlisten"+habitList);
    }

    public void prepareButtonsForRemoval(Context context) {
        for (HabitButtonContainer i :
                habitList) {
            i.prepareForRemoval(context);
            Log.d("Datamanager", "Prepared habit "+i+" for removal");
        }
    }
    public void undoPrepareButtonsForRemoval(Context context) {
        for (HabitButtonContainer i :
                habitList) {

            i.getHabit().updateHabit(context, i.getView());
        }

    }
    public void removeHabit(HabitButtonContainer habit) {
        removeHabitFromDatabas(habit);
        habitList.remove(habit);
        habit.delete();
    }

    public HabitButtonContainer getHabitButtonContainer(View view,Context context) {

        for (HabitButtonContainer i :
                habitList) {

            if (i.getButton(context) == view) {
                Log.d("Datamanager", "Returned "+i);
                return i;
            }
        }
        return null;
    }

    public void saveHabits() {


    }
    public void retrieveHabits() {

    }
    public void updateDatabase() {

        ValueEventListener listener = new ValueEventListener() {
            ArrayList<Object> list = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isWritingToDatabase || firstDataChange) {
                    Log.d("FirebaseDatamanager", "Der er ændret i databasen!");
                    for (DataSnapshot habitSnapshot : dataSnapshot.getChildren()) {
                        addHabit(makeHabitFromSnapshot(habitSnapshot));

                    }
                    context.habitlistLayout.removeAllViews();
                    context.addHabits();


                    firstDataChange = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseDatamanager","Recieved error"+databaseError.toException());
            }
        };
        DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    isWritingCounter++;
                    if (isWritingCounter == 4) {
                        isWritingToDatabase = false;
                        isWritingCounter = 0;
                    }
                    //Log.d("Datamanager", "Gemte data korrekt. Nu er isWriting.."+isWritingToDatabase);
                }
            }
        };




        Log.d("FirebaseDatamanager", "Printer følgende liste: "+ habitList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference habitref2;
        database.getReference().addValueEventListener(listener);
        isWritingToDatabase = true;

        for (HabitButtonContainer i : habitList) {
            habitref2 = database.getReference(i.getHabit().getTitel());
            habitref2.setValue(null);
            habitref2.child("repetitions").setValue(i.getHabit().getRepetitions(),completionListener);
            habitref2.child("title").setValue(i.getHabit().getTitel(),completionListener);
            habitref2.child("isCompleted").setValue(i.getHabit().isCompletedObjective(),completionListener);
            habitref2.child("timeStampList").setValue(i.getHabit().getCheckedTimeStamps(),completionListener);


        }





    }

    public void removeHabitFromDatabas(HabitButtonContainer habitButtonContainer) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference habitref2 = database.getReference(habitButtonContainer.getHabit().getTitel());
        habitref2.removeValue();
    }


    private HabitButtonContainer makeHabitFromSnapshot(DataSnapshot snapshot) {
            String titel = "";
            boolean state = false;
            int reps = 0;
            ArrayList<Long> dateList = new ArrayList<>();
            Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
            while (iterator.hasNext()) {
               DataSnapshot i = iterator.next();

                if (i.getKey().equals("isCompleted")) {
                    state = (Boolean) i.getValue();
                    //Log.d("Datamanager", "Fandt staten!!" + state);
                }
                if (i.getKey().equals("repetitions")) {
                    reps = safeLongToInt((Long)i.getValue());
                    //Log.d("Datamanager", "Fandt reps!" + reps);
                }

                if (i.getKey().equals("title")) {
                    titel = (String) i.getValue();
                    //Log.d("Datamanager", "Fandt titlen!" + state);
                }
                if (i.getKey().equals("timeStampList")) {
                    dateList = (ArrayList<Long>) i.getValue();
                    //Log.d("Datamanager", "Fandt timestamplisten!" + dateList);
                }


                }
        Habit h = new Habit(reps,titel,state, dateList);
        LinearLayout layout = h.createLayout(context);
        if (state) {
            h.completeObjective(layout);
        } else {
            h.undoCompleteObjective(layout, h.isItUnderADaySinceLastPress());

        }
        return new HabitButtonContainer(h, layout);

    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}
