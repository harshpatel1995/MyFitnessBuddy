package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Intent;


import java.text.SimpleDateFormat;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.ArrayList;


public class StartWorkout extends AppCompatActivity {

    //preferences for the system
    SharedPreferences sharedPreference;
    //database for system
    SQLiteDatabase database;


    String workoutNameStr, formattedDateStr;
    ListView startWorkoutLV, previewLogLV;
    TextView startWorkoutNameTV;
    RelativeLayout previewLayout;
    LinearLayout mainButtonsLayout;
    ArrayAdapter<String> adapter;// = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);
    ArrayAdapter<String> adapterForPreview; // = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedSetsAsStringsList);

    final ArrayList<String> savedSetsAsStringsList = new ArrayList<>();
    final ArrayList<String> exerciseList = new ArrayList<>();

    //IMPORTANT
    //can be accessed by any class to populated exercises
    public static final ArrayList<ExerciseSet> workoutAsListOfSetsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Here we instantiate the sharedPreference by giving it the package name
        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        workoutNameStr = sharedPreference.getString(ConstantValues.cSP_STARTED_WORKOUT, "Workout");
        formattedDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Start Workout");


        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        previewLayout = (RelativeLayout) findViewById(R.id.startWorkoutPreviewLayout);
        previewLogLV =(ListView) findViewById(R.id.startWorkoutLogsSetsLV);

        mainButtonsLayout =(LinearLayout) findViewById(R.id.startWorkoutLinearButtonLayout);

        startWorkoutLV = (ListView) findViewById(R.id.startWorkoutView);
        startWorkoutNameTV = (TextView) findViewById(R.id.startWorkoutNameTV);
        startWorkoutNameTV.setText(workoutNameStr);


        // final ArrayList <String> myExerciseList = new ArrayList<>();
        populateExercisesListView();

        registerForContextMenu(startWorkoutLV);

        startWorkoutLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Exercise Tapped: ", exerciseList.get(position));

                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString(ConstantValues.cSP_CURRENT_EXERCISE_TO_LOG, exerciseList.get(position));
                editor.apply();

                Intent logSets = new Intent(getApplicationContext(), LogSets.class);
                startActivity(logSets);
            }
        });
    }

    public void populateExercisesListView(){

        //String workoutNameStr = sharedPreference.getString(ConstantValues.cSP_PREVIEW_WORKOUT, "");
        boolean isPredefinedWorkoutBln = sharedPreference.getBoolean(ConstantValues.cSP_IS_PREVIEW_FOR_PREDEFINED, false);
        String tableNameStr;
        String exercisesToAddStr = "";

        //create array to store exercise names
        //final ArrayList<String> exerciseList = new ArrayList<>();

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
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);

                startWorkoutLV.setAdapter(adapter);


            }
            else{
                Log.i("Info", "SQL query failed");
            }

        }
        else{
            Log.i("Error", "Database is null.");
        }
    }

    //saves the workout from the button click
    public void onSaveWorkoutClick(View view){

        //add workout to database
        addWorkoutToDatabase(workoutAsListOfSetsList);

        //clear list of sets so it can be repopulated the next time
        workoutAsListOfSetsList.clear();

        //go back to home page (user profile)
        Intent userProfile = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(userProfile);

    }

    //cancels the workout from the button click
    public void onBackOrCancelClick(View view){


        //clear list of sets so it can be repopulated the next time
        workoutAsListOfSetsList.clear();

        //ends the activity and goes back to the previous page
       finish();

    }

    //adds all sets of a workout to the database
    //workout must be presented as a list of sets
    public void addWorkoutToDatabase(ArrayList<ExerciseSet> workoutSetsList){

        for (ExerciseSet setObj : workoutSetsList)
        {
            String setExerciseNameStr = setObj.getExercise();
            int setRepsInt = setObj.getReps();
            int setWeightInt = setObj.getWeight();

            addSetToDatabase(setExerciseNameStr,setRepsInt,setWeightInt);
        }

    }

    //adds a single set to the database logs table
    public void addSetToDatabase(String exerciseNameStr, int repsInt, int weightInt){

        try {

            //SQL to insert the set into the database
            String sqlCodeStr = "INSERT INTO logs (date, workout, exercise, reps, weight) VALUES ('" + formattedDateStr
                    + "', '" + workoutNameStr + "', '" + exerciseNameStr + "', " + repsInt + ", " + weightInt + ")";

            //adds a new record for the workout
            database.execSQL(sqlCodeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //user hits preview
    public void onPreviewLoggedWorkoutClick(View view){
        previewLayout.setVisibility(View.VISIBLE);
        previewLogLV.setVisibility(View.VISIBLE);
        startWorkoutLV.setVisibility(View.INVISIBLE);
        mainButtonsLayout.setVisibility(View.INVISIBLE);
    }

    //user hits back button
    public void onPreviewLoggedWorkoutBackClick(View view){
        previewLayout.setVisibility(View.INVISIBLE);
        previewLogLV.setVisibility(View.INVISIBLE);
        startWorkoutLV.setVisibility(View.VISIBLE);
        mainButtonsLayout.setVisibility(View.VISIBLE);
    }



}
