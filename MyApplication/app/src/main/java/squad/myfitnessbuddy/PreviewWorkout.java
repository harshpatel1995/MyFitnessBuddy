package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class   PreviewWorkout extends AppCompatActivity {

    //database for system
    SQLiteDatabase database;
    SharedPreferences sharedPreference;

    ListView exercisesLV;
    TextView workoutNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Preview Workout");

        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        exercisesLV = (ListView) findViewById(R.id.previewWorkoutExerciseLV);
        workoutNameTV = (TextView) findViewById(R.id.previewWorkoutNameTV);
        workoutNameTV.setText(sharedPreference.getString(ConstantValues.cSP_PREVIEW_WORKOUT, ""));

        populateExercisesListView();
    }

    //go back a page
    public void onBackButtonClicked (View view){
        finish();
    }

    //go to max rep stats page
    public void onViewStatsClicked (View view){
        Intent graph = new Intent(this, Graph.class);
        startActivity(graph);
    }

    //populates the listview on the page
    public void populateExercisesListView(){

        String workoutNameStr = sharedPreference.getString(ConstantValues.cSP_PREVIEW_WORKOUT, "");
        boolean isPredefinedWorkoutBln = sharedPreference.getBoolean(ConstantValues.cSP_IS_PREVIEW_FOR_PREDEFINED, false);
        String tableNameStr;
        String exercisesToAddStr = "";

        //create array to store exercise names
        final ArrayList<String> exerciseList = new ArrayList<>();

        if (isPredefinedWorkoutBln){
            tableNameStr = "predefinedWorkouts";
        }
        else{
            tableNameStr = "savedWorkouts";
        }

        if (database != null) {
            try {
                //set cursor to start traversing search
                //raw query is SQL code (Select everything from table "exercises" and order them by name)
                Cursor c = database.rawQuery("SELECT * FROM " + tableNameStr + " WHERE name = '" + workoutNameStr + "'", null);

                //get items from "name" column of table
                int exerciseIndex = c.getColumnIndex("exercises");

                //move cursor to to of list (table)
                c.moveToFirst();

                    exercisesToAddStr = c.getString(exerciseIndex);

            }catch(Exception e){
                e.printStackTrace();
            }
                if(!exercisesToAddStr.equals("")) {
                    //separate the exercises (pipe character must be escaped)
                    String[] exerciseArray = exercisesToAddStr.split("\\|");

                    for (String individualExercise : exerciseArray) {
                        exerciseList.add(individualExercise);
                    }

                    //adapter to get list into ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, exerciseList);
                    exercisesLV.setAdapter(adapter);
                    exercisesLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                }
            else{
                    Log.i("Info","SQL query failed");
                }

        }
        else{
            Log.i("Error", "Database is null.");
        }

    }

}
