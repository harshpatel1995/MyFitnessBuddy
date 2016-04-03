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
        }
        catch (Exception e){
            e.printStackTrace();
        }

        populateLogs();
    }

    protected void populateLogs() {

        int id;
        String date;
        String exerciseName;
        int reps;
        int weight;

        Cursor c = exerciseDB.rawQuery("SELECT * FROM logs", null);
        int idColumn = c.getColumnIndex("_id");
        int dateColumn = c.getColumnIndex("date");
        int exerciseColumn = c.getColumnIndex("exercise");
        int repsColumn = c.getColumnIndex("reps");
        int weightColumn = c.getColumnIndex("weight");

        c.moveToLast();

        while(c != null) {
            id = c.getInt(idColumn);
            date = c.getString(dateColumn);
            exerciseName = c.getString(exerciseColumn);
            reps = c.getInt(repsColumn);
            weight = c.getInt(weightColumn);
            System.out.println(id + " " + date + " " + exerciseName + " " + reps + " " + weight +" ");
            c.moveToPrevious();
        }

        c.close();
    }
}
