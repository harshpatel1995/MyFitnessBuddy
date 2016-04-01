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
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class CreateWorkout extends AppCompatActivity {

    public ListView exerciseLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000"))); // set your desired color
        actionBar.setTitle("Create Workout");

        Intent intent = getIntent();

        exerciseLV = (ListView) findViewById(R.id.exercisesLV);

        //populate list of exercises
        populateExercises();


    }

    //Method to populate list of exercises
    protected void populateExercises(){
        //create array to store exercise names
        final ArrayList<String> exercisesList = new ArrayList<>();

        //create the database manager
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        //create database if it does not exist (or was erased)
        //open if it is already there
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        try {
            //get actual database from manager (the one we just opened)
            SQLiteDatabase exerciseDB = myDbHelper.getReadableDatabase();

            //set cursor to start traversing search
            //raw query is SQL code (Select everything from table "exercises" and order them by name)
            Cursor c = exerciseDB.rawQuery("SELECT * FROM exercises ORDER BY name", null);

            //get items from "name" column of table
            int nameIndex = c.getColumnIndex("name");

            //move cursor to to of list (table)
            c.moveToFirst();

            //add exercise to list and move cursor to next exercise
            while (c != null) {

               exercisesList.add(c.getString(nameIndex));
                c.moveToNext();
            }

            //close database to avoid memory leak
            exerciseDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //adapter to get list into ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, exercisesList);
        exerciseLV.setAdapter(adapter);
        //check multiple exercises at a time
        exerciseLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    //************Still Working On  @@@Brandon
    //Code to save a new workout (will add to database)
    public void saveCustomWorkout(View view)
    {
        EditText workoutNameED = (EditText) findViewById(R.id.createWorkoutName);
        String workoutNameStr = workoutNameED.getText().toString();

        SparseBooleanArray checked = exerciseLV.getCheckedItemPositions();

        if (workoutNameStr == ""){
            Toast.makeText(getApplicationContext(),"The workout must have a name.",Toast.LENGTH_SHORT).show();
        }
        else if(checked.size()==0){
            Toast.makeText(getApplicationContext(),"You must select at least one exercise.",Toast.LENGTH_SHORT).show();
        }
        else {
            String exercisesForWorkoutStr = "";

            for (int i = 0; i < exerciseLV.getAdapter().getCount(); i++) {
                if (checked.get(i)) {
                    exercisesForWorkoutStr += exerciseLV.getItemAtPosition(i).toString() + "|";
                }
            }
            exercisesForWorkoutStr = exercisesForWorkoutStr.substring(0, exercisesForWorkoutStr.length() - 1);
            Log.i("Test", exercisesForWorkoutStr);
        }

    }




}


