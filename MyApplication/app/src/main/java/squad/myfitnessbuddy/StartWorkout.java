package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StartWorkout extends AppCompatActivity {

    //preferences for the system
    SharedPreferences sharedPreference;
    //database for system
    SQLiteDatabase database;

    String workoutNameStr;



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

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(workoutNameStr);


        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ListView startWorkoutView = (ListView) findViewById(R.id.startWorkoutView);


        final ArrayList <String> myExerciseList = new ArrayList<>();
        

    }


    //adds a single set to the database logs table
    public void addSetToDatabase(String exerciseNameStr, Integer repsInt, Integer weightInt){


        String formattedDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try {

            //SQL to insert the set into the database
            String sqlCodeStr = "INSERT INTO logs (date, exercise, reps, weight) VALUES ('" + formattedDateStr
                    + "', '" + exerciseNameStr + "', " + repsInt + ", " + weightInt + ")";

            //adds a new record for the workout
            database.execSQL(sqlCodeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
