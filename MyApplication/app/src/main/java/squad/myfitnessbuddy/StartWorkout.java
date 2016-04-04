package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import android.widget.TextView;
import java.util.Date;
import java.util.ArrayList;

import android.widget.Toast;


public class StartWorkout extends AppCompatActivity {

    //preferences for the system
    SharedPreferences sharedPreference;
    //database for system
    SQLiteDatabase database;

    String workoutNameStr, formattedDateStr;
    ListView startWorkoutLV;
    TextView startWorkoutNameTV;
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
        formattedDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Log Workout");

        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        startWorkoutLV = (ListView) findViewById(R.id.startWorkoutView);
        startWorkoutNameTV = (TextView) findViewById(R.id.startWorkoutNameTV);
        startWorkoutNameTV.setText(workoutNameStr);


       // final ArrayList <String> myExerciseList = new ArrayList<>();
        populateExercisesListView();


        startWorkoutLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Exercise Tapped: ", exerciseList.get(position));
                Toast.makeText(getApplicationContext(), "This feature coming soon.", Toast.LENGTH_LONG).show();
            }

        });


        addSetToDatabase("Test Exercise", 10, 250);
    }

    public void populateExercisesListView(){

        //String workoutNameStr = sharedPreference.getString(ConstantValues.cSP_PREVIEW_WORKOUT, "");
        String tableNameStr;
        String exercisesToAddStr = "";

        //create array to store exercise names
        //final ArrayList<String> exerciseList = new ArrayList<>();

            tableNameStr = "savedWorkouts";


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

                c.close();

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



    //adds a single set to the database logs table
    public void addSetToDatabase(String exerciseNameStr, Integer repsInt, Integer weightInt){

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



}
