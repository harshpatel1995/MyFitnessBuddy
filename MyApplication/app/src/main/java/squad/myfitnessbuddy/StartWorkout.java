package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;


public class StartWorkout extends AppCompatActivity {

    //preferences for the system
    SharedPreferences sharedPreference;
    //database for system
    SQLiteDatabase database;

    String workoutNameStr;
    ListView startWorkoutView;
    TextView startWorkoutNameTV;
    String date;
    ArrayAdapter<String> adapter;// = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);
    final ArrayList<String> exerciseList = new ArrayList<>();

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
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(workoutNameStr+date);


        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        startWorkoutView = (ListView) findViewById(R.id.startWorkoutView);
        startWorkoutNameTV = (TextView) findViewById(R.id.startWorkoutNameTV);
        startWorkoutNameTV.setText(workoutNameStr);


       // final ArrayList <String> myExerciseList = new ArrayList<>();
        populateExercisesListView();


        startWorkoutView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Exercise Tapped: ", exerciseList.get(position));
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

                startWorkoutView.setAdapter(adapter);







            }
            else{
                Log.i("Info", "SQL query failed");
            }

        }
        else{
            Log.i("Error", "Database is null.");
        }

    }



    public void addSetToDatabase(String exerciseNameStr, Integer repsInt, Integer weightInt){

        
    }



}
