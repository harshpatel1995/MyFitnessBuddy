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
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyLogs extends MenuButtonBar {

    SQLiteDatabase exerciseDB;

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

        try {
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
            exerciseDB.execSQL("DELETE FROM logs");
            exerciseDB.execSQL("INSERT INTO logs (date, exercise, reps, weight) VALUES ('2016-04-03', 'Squats', 8, 200)");
            exerciseDB.execSQL("INSERT INTO logs (date, exercise, reps, weight) VALUES ('2016-04-05', 'Bench Press', 7, 100)");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        populateLogs();
    }

    protected void populateLogs() {

        try {
            int id;
            String dateString;
            String exerciseName;
            int reps;
            int weight;

            Cursor c = exerciseDB.rawQuery("SELECT * FROM logs", null);
            int idColumn = c.getColumnIndex("_id");
            int dateColumn = c.getColumnIndex("date");
            int exerciseColumn = c.getColumnIndex("exercise");
            int repsColumn = c.getColumnIndex("reps");
            int weightColumn = c.getColumnIndex("weight");


            for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {
                id = c.getInt(idColumn);
                dateString = c.getString(dateColumn);
                exerciseName = c.getString(exerciseColumn);
                reps = c.getInt(repsColumn);
                weight = c.getInt(weightColumn);

                Date date = getDate(dateString);

                //For testing purposes
                System.out.println(id + " " + date.toString() + " " + exerciseName + " " + reps + " " + weight + " ");
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

}
