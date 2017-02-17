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
            i.getHabit().updateHabit(context);
        }

    }
    public void removeHabit(HabitButtonContainer habit) {
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
                        for (DataSnapshot propertySnapshot : habitSnapshot.getChildren()) {
                            Log.d("FirebaseDatamanager", "Child til snapshottet: " + propertySnapshot.getValue());
                            list.add(propertySnapshot.getValue());
                        }
                    }
                    Log.d("FirebaseDatamanager", "Listen ======== " + list);
                    makeHabitsFromList(list);
                    list.clear();
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
                    if (isWritingCounter == 3) {
                        isWritingToDatabase = false;
                        isWritingCounter = 0;
                    }
                    Log.d("Datamanager", "Gemte data korrekt. Nu er isWriting.."+isWritingToDatabase);
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

        }





    }

    private void makeHabitsFromList(ArrayList<Object> list) {
        habitList.clear();
        if (list.size()%3!=0 || list.size()<3) {
            Log.d("Datamanager","Der er enten for mange eller for få elementer i listen...   Antal elementer: "+list.size());
        } else {
            String titel = "";
            boolean state = false;
            int reps = 0;
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) instanceof Boolean) {

                    state = (Boolean) list.get(i);
                    Log.d("Datamanager", "Fandt en Boolean!"+state);
                }  else if (list.get(i) instanceof Long) {

                    reps = safeLongToInt((long)list.get(i));
                    Log.d("Datamanager", "Fandt en Integer!"+reps);
                }
                else if (list.get(i) instanceof String) {

                    titel = (String) list.get(i);
                    Log.d("Datamanager", "Fandt en String!"+titel);

                    Habit h = new Habit(reps,titel,state);
                    LinearLayout layout = h.createLayout(context);

                    addHabit(new HabitButtonContainer(h, layout));
                    context.habitlistLayout.removeAllViews();
                    context.addHabits();
                    if (state) {
                        h.completeObjective(layout);
                    } else {
                        h.undoCompleteObjective(layout);
                    }

                }
            }
        }
    }
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}
