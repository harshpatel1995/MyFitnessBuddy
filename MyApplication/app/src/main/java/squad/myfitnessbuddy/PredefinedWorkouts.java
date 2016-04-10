package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class PredefinedWorkouts extends AppCompatActivity {

    ListView workoutLV;
    SQLiteDatabase exerciseDB;
    SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predefined_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Predefined Workouts");

        try {
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        workoutLV = (ListView) findViewById(R.id.workoutLV);
        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        populateWorkouts();
    }

    //Method to populate list of predefined workouts
    protected void populateWorkouts() {
        //create array to store Predefined Workout names
        final ArrayList<String> predefinedWorkoutList = new ArrayList<>();

        try {
            //set cursor to start traversing search
            //raw query is SQL code (Select everything from table "predefinedWorkouts")
            Cursor c_v2 = exerciseDB.rawQuery("SELECT * FROM predefinedWorkouts", null);
            //get items from "name" column of table
            int idxName = c_v2.getColumnIndex("name");
            while (c_v2 != null &&  c_v2.moveToNext()) {
                predefinedWorkoutList.add(c_v2.getString(idxName));
            }
            //close cursor to avoid memory leak
            c_v2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //adapter to get list into ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, predefinedWorkoutList);
        workoutLV.setAdapter(adapter);
        //only one workout can be checked at a time
        workoutLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    //preview the workout from the predefinedWorkouts page
    public void previewPredefinedWorkoutButton(View view) {
       String selectedWorkoutName = getCheckedItemName(workoutLV);

       if (!selectedWorkoutName.equals("")) {

           SharedPreferences.Editor editor = sharedPreference.edit();

           //Use the editor to store the name of the current workout to preview in the SharedPreference
           editor.putString(ConstantValues.cSP_PREVIEW_WORKOUT, selectedWorkoutName);
           editor.putBoolean(ConstantValues.cSP_IS_PREVIEW_FOR_PREDEFINED,true);
           editor.apply();

           //open preview page
           Intent previewWorkout = new Intent(getApplicationContext(), PreviewWorkout.class);
           startActivity(previewWorkout);
       }
       else{
           //if nothing selected, display error message
           Toast.makeText(getApplicationContext(),"Please select a workout to preview.",Toast.LENGTH_SHORT).show();
       }

   }

    //returns the name of the item that is checked in the list.
    //returns empty string if it noting is checked
    public String getCheckedItemName(ListView listView)
    {
        String selectedItem = "";

        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

        //iterate through list
        for(int counter = 0; counter < listView.getCount(); counter++){
            //if something is checked get name of item
            if (sparseBooleanArray.get(counter)){
                selectedItem = listView.getItemAtPosition(counter).toString();
                break;
            }
        }
        return selectedItem;
    }

    //go back
    public void predefinedWorkoutBackButton(View view){
        finish();
    }

    public void selectPredefinedWorkoutButton(View view) {
        //create an array to hold the checked predefined workout
        SparseBooleanArray checked = workoutLV.getCheckedItemPositions();
        //String to hold the list of exercersises for the selected workout (as one long string)
        String exerciseString = "";
        //hold the string name of the predefined workout
        String selectedWorkoutName = "";

        //if no predefined workout was checked
        if (checked.size() < 1) {
            Toast.makeText(getApplicationContext(), "You must select a workout.", Toast.LENGTH_SHORT).show();
        } else {
            //get the string name of the selected predefined workout
            for (int i = 0; i < workoutLV.getAdapter().getCount(); i++) {
                if (checked.get(i)) {
                    selectedWorkoutName = workoutLV.getItemAtPosition(i).toString();
                }
            }
            boolean isOriginalName = true;
            if (workoutAlreadyInDataBase(selectedWorkoutName)) {
                isOriginalName = false;
            }
            //fetch the exercises of the selected predefined workout if the workout doesnt already exist in the saved workouts table
            if (isOriginalName) {
                try {
                    Cursor C = exerciseDB.rawQuery("SELECT * FROM predefinedWorkouts WHERE name = '" + selectedWorkoutName + "'", null);
                    //get items from "exercises" column of table
                    int exerciseIndex = C.getColumnIndex("exercises");
                    //move cursor to top of list (table)
                    C.moveToFirst();
                    //save the exercise string
                    exerciseString = C.getString(exerciseIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //save the predefined workout into the "savedWorkouts" table
                savePredefinedWorkoutInDataBase(selectedWorkoutName, exerciseString);
                Intent savedWorkouts = new Intent(getApplicationContext(), SavedWorkouts.class);
                startActivity(savedWorkouts);
            }
        }
    }

    public void savePredefinedWorkoutInDataBase(String workoutNameStr, String exercisesStr){
        try {
            //adds a new record for the workout in the "savedWorkouts" table
            exerciseDB.execSQL("INSERT INTO savedWorkouts (name, exercises) VALUES ('" + workoutNameStr
                    + "', '" + exercisesStr + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean workoutAlreadyInDataBase(String workoutNameStrToCheck)
    {
        //counts number of entries in database with same name
        Cursor mCount= exerciseDB.rawQuery("SELECT count(*) FROM savedWorkouts WHERE name = '" + workoutNameStrToCheck + "'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        if (count!=0){
            Toast.makeText(getApplicationContext(), "\"" + workoutNameStrToCheck + "\" already exists in your saved workouts.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }

    }
}


