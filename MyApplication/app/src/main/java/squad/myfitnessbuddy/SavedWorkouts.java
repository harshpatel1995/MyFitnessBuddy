package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class SavedWorkouts extends MenuButtonBar {
    LinearLayout createWorkoutPopup;
    //database for system
    SQLiteDatabase database;
    //listview control that displays exercises on screen
    public ListView savedWorkOutsLV;

    //This will let you choose between create or predefined, for now just goes to create page
    public void addWorkoutOnClick(View view) {
        createWorkoutPopup.setVisibility(View.VISIBLE);
    }

    public void customizedWorkoutButton (View view){
        Intent addWorkout = new Intent(getApplicationContext(), CreateWorkout.class);
        startActivity(addWorkout);
    }

    public void PredefinedWorkoutOnClick(View view) {
        Intent predefinedWorkout = new Intent(getApplicationContext(), PredefinedWorkouts.class);
        startActivity(predefinedWorkout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.iconv2);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Saved Workouts");

        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        createWorkoutPopup = (LinearLayout) findViewById(R.id.createWorkoutLayout);

        savedWorkOutsLV = (ListView) findViewById(R.id.savedWorkoutsLV);
        //populate list of exercises
        populateExercises();

    }


    //Method to populate list of exercises
    protected void populateExercises() {
        //create array to store exercise names
        final ArrayList<String> workoutsList = new ArrayList<>();

        if (database != null) {
        try {
                //set cursor to start traversing search
                //raw query is SQL code (Select everything from table "exercises" and order them by name)
                Cursor c = database.rawQuery("SELECT * FROM savedWorkouts ORDER BY name", null);

                //get items from "name" column of table
                int nameIndex = c.getColumnIndex("name");

                //move cursor to to of list (table)
                c.moveToFirst();

                //add exercise to list and move cursor to next exercise
                while (c != null) {

                    workoutsList.add(c.getString(nameIndex));
                    c.moveToNext();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            //adapter to get list into ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workoutsList);
            savedWorkOutsLV.setAdapter(adapter);
            //check multiple exercises at a time
            savedWorkOutsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }
        else{
            Log.i("Error", "Database is null.");
        }
    }




}
