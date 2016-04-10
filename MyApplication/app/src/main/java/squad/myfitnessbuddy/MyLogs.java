package squad.myfitnessbuddy;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;


public class MyLogs extends MenuButtonBar implements AdapterView.OnItemSelectedListener {

    SQLiteDatabase exerciseDB;
    Spinner timeSpinner, filterBySpinner, filterOptionsSpinner;
    TreeSet<String> exerciseSet, workoutSet, bodypartSet;
    String[] timeOptions = new String[] {"7 Days", "15 Days", "1 Month", "3 Months", "All Logs"};
    String[] filterByOptions = new String[] {"", "Exercise", "Workout", "Body Part"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_logs);

        logsButtonTV = (TextView) findViewById(R.id.logsButtonBarText);
        logsButtonIV = (ImageView) findViewById(R.id.logsButtonBarIcon);
        buttonBarL = (LinearLayout) findViewById(R.id.logsButtonBar);
        buttonBarSnackL = (TextView) findViewById(R.id.logButtonBarSnack);
        buttonBarShaderL = (TextView) findViewById(R.id.logButtonBarShader);

        logsButtonTV.setTextColor(ContextCompat.getColor(this, R.color.windowBackground));
        buttonBarL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonPressed));
        buttonBarSnackL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBarSnackPressed));
        buttonBarShaderL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBarShaderPressed));

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            logsButtonIV.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.log_button_active));
        } else {
            logsButtonIV.setBackground(ContextCompat.getDrawable(this, R.drawable.log_button_active));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("My Logs");


        try {
            //test code
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
            exerciseDB.execSQL("DELETE FROM logs");
            exerciseDB.execSQL("INSERT INTO logs (date, workout,  exercise, reps, weight) VALUES ('2016-04-01', 'Leg Day', 'Squats', 8, 200)");
            exerciseDB.execSQL("INSERT INTO logs (date, workout,  exercise, reps, weight) VALUES ('2016-03-28', 'Chest Blast', 'Arnold Press', 7, 100)");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        exerciseSet = new TreeSet<>();
        workoutSet = new TreeSet<>();
        bodypartSet = new TreeSet<>();

        populateFilters();

        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> timeSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, timeOptions);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeSpinner.setAdapter(timeSpinnerAdapter);
        timeSpinner.setOnItemSelectedListener(this);

        filterBySpinner = (Spinner) findViewById(R.id.filterBySpinner);
        ArrayAdapter<CharSequence> filterByAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, filterByOptions);
        filterByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        filterBySpinner.setAdapter(filterByAdapter);
        filterBySpinner.setOnItemSelectedListener(this);

        filterOptionsSpinner = (Spinner) findViewById(R.id.filterOptionsSpinnner);
    }

    public String timeString() {
        switch(timeSpinner.getSelectedItemPosition()) {
            case 0: return ConstantValues.cFETCH_LOGS_LAST_7DAYS;
            case 1: return ConstantValues.cFETCH_LOGS_LAST_15DAYS;
            case 2: return ConstantValues.cFETCH_LOGS_LAST_1MONTH;
            case 3: return ConstantValues.cFETCH_LOGS_LAST_3MONTHS;
            case 4: return ConstantValues.cFETCH_LOGS_ALL;
            default: return "";
        }
    }

    public void onQueryClick(View view) {

        String queryString;

        String filterBySelected = filterBySpinner.getSelectedItem().toString();
        String filterOptionsSelected = filterOptionsSpinner.getSelectedItem().toString();
        if(filterBySelected == "" || filterOptionsSelected == "") {
            populateLogs(timeString());
        }

        else {
            switch(filterBySpinner.getSelectedItemPosition()) {
                case 1: queryString = timeString() + "AND exercise = '" + filterOptionsSelected + "'";
                    populateLogs(queryString);
                    break;
                case 2: queryString = timeString() + "AND workout = '" + filterOptionsSelected + "'";
                    populateLogs(queryString);
                    break;
                case 3: //queryString = "SELECT * FROM logs, exercises WHERE logs.name = exercises.name AND exercises.primary = '" + filterOptionsSelected +  "'";
                        //"SELECT * FROM logs LEFT OUTER JOIN exercises ON exercises.name = logs.exercise WHERE exercises.primary = '" + filterOptionsSelected +  "'"
                    //"SELECT * FROM logs, exercises WHERE exercises.name = logs.exercise AND exercises.primaryWorked = '" + filterOptionsSelected +  "'";
                        queryString = "SELECT * FROM logs LEFT OUTER JOIN exercises ON exercises.name = logs.exercise WHERE exercises.primaryWorked = '" + filterOptionsSelected +  "'";
                    populateLogs(queryString);

                    break;
            }
        }
    }

    protected void setFilterOptions(String[] array) {
        ArrayAdapter<CharSequence> filterOptionsAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, array);
        filterOptionsSpinner.setAdapter(filterOptionsAdapter);
        filterOptionsSpinner.setOnItemSelectedListener(this);
    }

    protected void populateLogs(String queryString) {

        try {
            int id;
            String workoutName;
            String dateString;
            String exerciseName;
            int reps;
            int weight;

            Cursor c = exerciseDB.rawQuery(queryString, null);
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

                //For testing purposes, print to the console
                System.out.println(id + " " + date.toString() + " " + workoutName + " " + exerciseName + " " + reps + " " + weight + " ");
            }

            c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFilters() {

        try {
            exerciseSet.add("");
            workoutSet.add("");
            bodypartSet.add("");

            Cursor c = exerciseDB.rawQuery("SELECT * FROM exercises", null);
            int exerciseIndex = c.getColumnIndex("name");
            int primaryIndex = c.getColumnIndex("primaryWorked");
            while(c != null && c.moveToNext()){
                exerciseSet.add(c.getString(exerciseIndex));
                bodypartSet.add(c.getString(primaryIndex));
            }
            c.close();

            //should optimize more
            Cursor c_v2 = exerciseDB.rawQuery("SELECT logs.workout FROM logs LIMIT 5000", null);
            int idxName = c_v2.getColumnIndex("workout");
            while (c_v2 != null &&  c_v2.moveToNext()) {
                workoutSet.add(c_v2.getString(idxName));

            }
            c_v2.close();

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
        if(parent.getId() == R.id.filterBySpinner) {
            switch(position) {
                case 0:
                    setFilterOptions(new String[] {""});
                    break;
                case 1:
                    setFilterOptions(exerciseSet.toArray(new String[exerciseSet.size()]));
                    break;
                case 2:
                    setFilterOptions(workoutSet.toArray(new String[workoutSet.size()]));
                    break;
                case 3:
                    setFilterOptions(bodypartSet.toArray(new String[bodypartSet.size()]));
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

