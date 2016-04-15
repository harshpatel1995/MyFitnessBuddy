package squad.myfitnessbuddy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class MyLogs extends MenuButtonBar implements AdapterView.OnItemSelectedListener {

    //Listview that displays the results of the user's queries
    public ListView listView;

    SQLiteDatabase exerciseDB;
    Spinner timeSpinner, filterBySpinner, filterOptionsSpinner;
    TreeSet<String> exerciseSet, workoutSet, bodypartSet;
    String[] timeOptions = new String[] {"Past Week", "Past 2 Weeks", "Past Month", "Past 3 Months", "All Logs"};
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
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
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

    @Override
    protected void onRestart() {
        try {
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        exerciseSet = new TreeSet<>();
        workoutSet = new TreeSet<>();
        bodypartSet = new TreeSet<>();

        populateFilters();
        super.onRestart();
    }

    public String timeString() {
        switch(timeSpinner.getSelectedItemPosition()) {
            case 0:
                return ConstantValues.cFETCH_LOGS_LAST_7DAYS;
            case 1:
                return ConstantValues.cFETCH_LOGS_LAST_14DAYS;
            case 2:
                return ConstantValues.cFETCH_LOGS_LAST_1MONTH;
            case 3:
                return ConstantValues.cFETCH_LOGS_LAST_3MONTHS;
            case 4:
                return ConstantValues.cFETCH_LOGS_ALL;
            default:
                return "";
        }
    }

    public void onQueryClick(View view) {

        //listView.setAdapter(mAdapter);
        String queryString;
        String filterBySelected = filterBySpinner.getSelectedItem().toString();
        String filterOptionsSelected = filterOptionsSpinner.getSelectedItem().toString();

        if(filterBySelected.equals("") || filterOptionsSelected.equals("")) {
            populateLogs(timeString());
        }
        else {
            switch(filterBySpinner.getSelectedItemPosition()) {
                case 1:
                    queryString = timeString() + " AND exercise = '" + filterOptionsSelected + "'";
                    populateLogs(queryString);
                    break;
                case 2:
                    queryString = timeString() + " AND workout = '" + filterOptionsSelected + "'";
                    populateLogs(queryString);
                    break;
                case 3:
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

            CustomAdapter mAdapter = new CustomAdapter(this);
            this.listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(mAdapter);

            Cursor c = exerciseDB.rawQuery(queryString, null);

            if(c.getCount() == 0) {
                mAdapter.addItem("No results found.");
            }

            else {
                int dateColumn = c.getColumnIndex("date");
                int workoutColumn = c.getColumnIndex("workout");
                int exerciseColumn = c.getColumnIndex("exercise");
                int repsColumn = c.getColumnIndex("reps");
                int weightColumn = c.getColumnIndex("weight");

                ArrayList<Exercise> arrayList = new ArrayList<>();
                Exercise current, previous = null;

                for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {

                    workoutName = c.getString(workoutColumn);
                    dateString = c.getString(dateColumn);
                    exerciseName = c.getString(exerciseColumn);
                    reps = c.getInt(repsColumn);
                    weight = c.getInt(weightColumn);
                    Date date = getDate(dateString);

                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
                    String dtString = dateFormat.format(date);

                    if (previous == null) {
                        current = new Exercise(workoutName, exerciseName, reps, weight, dtString);
                        arrayList.add(current);
                        previous = current;
                    } else {
                        current = new Exercise(workoutName, exerciseName, reps, weight, dtString);

                        if (current.workoutName.equals(previous.workoutName) && current.date.equals(previous.date)) {
                            arrayList.add(current);
                        } else {
                            printArrayList(arrayList, mAdapter);
                            arrayList.clear();
                            arrayList.add(current);
                            previous = current;
                        }
                    }
                }
                if(!arrayList.isEmpty()) {
                    printArrayList(arrayList, mAdapter);
                    arrayList.clear();
                }
            }

            c.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void printArrayList(ArrayList<Exercise> arrayList, CustomAdapter mAdapter) {
        mAdapter.addSectionHeaderItem(arrayList.get(0).workoutName + " : " + arrayList.get(0).date);
        for(Exercise e : arrayList) {
            mAdapter.addItem(e.exerciseName + "   " + e.reps + " x " + e.weight + " lbs");
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
            Cursor c_v2 = exerciseDB.rawQuery("SELECT logs.workout FROM logs ORDER BY _id DESC LIMIT 5000", null);
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

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

}

class Exercise {

    public String workoutName;
    public String exerciseName;
    public int reps;
    public int weight;
    public String date;

    public Exercise(String workoutName, String exerciseName, int reps, int weight, String date) {
        this.workoutName = workoutName;
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
    }
}

