package squad.myfitnessbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyLogs extends MenuButtonBar implements AdapterView.OnItemSelectedListener {

    SQLiteDatabase exerciseDB;
    Spinner timeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_logs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("My Logs");

        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timeOptionsArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(this);

        try {
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
//            exerciseDB.execSQL("DELETE FROM logs");
//            exerciseDB.execSQL("INSERT INTO logs (date, workout,  exercise, reps, weight) VALUES ('2016-03-29', 'Leg Day', 'Squats', 8, 200)");
//            exerciseDB.execSQL("INSERT INTO logs (date, workout,  exercise, reps, weight) VALUES ('2016-03-28', 'Chest Blast', 'Bench Press', 7, 100)");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    protected void populateLogs(Cursor c) {

        try {
            int id;
            String workoutName;
            String dateString;
            String exerciseName;
            int reps;
            int weight;


            int idColumn = c.getColumnIndex("_id");
            int dateColumn = c.getColumnIndex("date");
            int workoutColumn = c.getColumnIndex("workout");
            int exerciseColumn = c.getColumnIndex("exercise");
            int repsColumn = c.getColumnIndex("reps");
            int weightColumn = c.getColumnIndex("weight");


            for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {
                id = c.getInt(idColumn);
                workoutName = c.getString(workoutColumn);
                dateString = c.getString(dateColumn);
                exerciseName = c.getString(exerciseColumn);
                reps = c.getInt(repsColumn);
                weight = c.getInt(weightColumn);

                Date date = getDate(dateString);

                //For testing purposes
                System.out.println(id + " " + date.toString() + " " + workoutName + " " + exerciseName + " " + reps + " " + weight + " ");
            }

            c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Date getDate(String dateString){
        SimpleDateFormat formattedDateStr = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition parsePosition = new ParsePosition(0);
        return formattedDateStr.parse(dateString, parsePosition);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = null;
        switch (position) {
            case 0:
                cursor = exerciseDB.rawQuery(ConstantValues.cFETCH_LOGS_LAST_7DAYS, null);
                break;
            case 1:
                cursor = exerciseDB.rawQuery(ConstantValues.cFETCH_LOGS_LAST_15DAYS, null);
                break;
            case 2:
                cursor = exerciseDB.rawQuery(ConstantValues.cFETCH_LOGS_LAST_1MONTH, null);
                break;
            case 3:
                cursor = exerciseDB.rawQuery(ConstantValues.cFETCH_LOGS_LAST_3MONTHS, null);
                break;
            case 4:
                cursor = exerciseDB.rawQuery(ConstantValues.cFETCH_LOGS_ALL, null);
                break;
        }
        populateLogs(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
