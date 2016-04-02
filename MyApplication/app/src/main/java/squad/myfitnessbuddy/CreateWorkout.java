package squad.myfitnessbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class CreateWorkout extends AppCompatActivity {

    //listview control that displays exercises on screen
    public ListView exerciseLV;

    //database
    public SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Create Workout");

        //gets intent of page that just called this one
        Intent intent = getIntent();

        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);

            //open database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        exerciseLV = (ListView) findViewById(R.id.exercisesLV);

        //populate list of exercises
        populateExercises();


    }

    //Method to populate list of exercises
    protected void populateExercises(){
        //create array to store exercise names
        final ArrayList<String> exercisesList = new ArrayList<>();


        try {

            //set cursor to start traversing search
            //raw query is SQL code (Select everything from table "exercises" and order them by name)
            Cursor c = database.rawQuery("SELECT * FROM exercises ORDER BY name", null);

            //get items from "name" column of table
            int nameIndex = c.getColumnIndex("name");

            //move cursor to to of list (table)
            c.moveToFirst();

            //add exercise to list and move cursor to next exercise
            while (c != null) {

               exercisesList.add(c.getString(nameIndex));
                c.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //adapter to get list into ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, exercisesList);
        exerciseLV.setAdapter(adapter);
        //check multiple exercises at a time
        exerciseLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    //Code to save a new workout (will add to database)
    public void saveCustomWorkout(View view)
    {
        String mustSelectExerciseStr = "You must select at least one exercise.";
        String mustSelectNameStr = "The workout must have a name.";

        //get workout name from user
        EditText workoutNameED = (EditText) findViewById(R.id.createWorkoutName);
        String workoutNameStr = workoutNameED.getText().toString();

        //boolean array for all list positions
        //true if checked, false if unchecked
        SparseBooleanArray checked = exerciseLV.getCheckedItemPositions();

        //there is no workout name
        if (workoutNameStr.equals("")){
            Toast.makeText(getApplicationContext(),mustSelectNameStr,Toast.LENGTH_SHORT).show();
        }
        //nothing was pressed yet in listview to populate the sparse boolean array
        else if(checked.size()<1){
            Toast.makeText(getApplicationContext(), mustSelectExerciseStr ,Toast.LENGTH_SHORT).show();
        }
        else {
            //list of exercises to store in database for workout
            String exercisesForWorkoutStr = "";

            for (int i = 0; i < exerciseLV.getAdapter().getCount(); i++) {
                if (checked.get(i)) {
                    exercisesForWorkoutStr += exerciseLV.getItemAtPosition(i).toString() + "|";
                }
            }
            //at least one exercise is checked
            if(!exercisesForWorkoutStr.equals("")){
            exercisesForWorkoutStr = exercisesForWorkoutStr.substring(0, exercisesForWorkoutStr.length() - 1);

                //save exercise to database
                boolean isAcceptableNameBln = true;
                boolean isOriginalName = true;

                if (containsSpecialCharacter(workoutNameStr))
                {
                    isAcceptableNameBln = false;
                }
                if(workoutAlreadyInDataBase(workoutNameStr))
                {
                    isOriginalName = false;
                }

                if(isAcceptableNameBln && isOriginalName) {
                    saveCustomWorkoutInDataBase(workoutNameStr, exercisesForWorkoutStr);

                    Intent savedWorkouts = new Intent(getApplicationContext(), SavedWorkouts.class);
                    startActivity(savedWorkouts);
                }
                else{
                    if(!isAcceptableNameBln) {
                        Toast.makeText(getApplicationContext(), "Name cannot cannot special characters.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "This exercise already exists.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            //no exercise is checked
            else{
                Toast.makeText(getApplicationContext(),mustSelectExerciseStr,Toast.LENGTH_SHORT).show();
            }

            }

    }

    //checks if there are any special character in string
    //not good for SQL
    public boolean containsSpecialCharacter(String string) {
        return (string == null) ? false : !string.matches("[A-Za-z0-9 ]*");
    }

    //Saves the workout in the database
    public void saveCustomWorkoutInDataBase(String workoutNameStr, String exercisesStr){

            try {

                //adds a new record for the workout
                database.execSQL("INSERT INTO savedWorkouts (name, exercises) VALUES ('" + workoutNameStr
                        + "', '" + exercisesStr + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    //go back a page
    public void cancelCustomWorkout(View view){
        finish();
    }


    //checks if exercise is already in the database
public boolean workoutAlreadyInDataBase(String workoutNameStrToCheck)
{
    //counts number of entries in daabase with same name
    Cursor mCount= database.rawQuery("SELECT count(*) FROM savedWorkouts WHERE name = '" + workoutNameStrToCheck + "'", null);
    mCount.moveToFirst();
    int count= mCount.getInt(0);
    mCount.close();

    if (count!=0){
        return true;
    }
    else{
        return false;
    }

    }
}




