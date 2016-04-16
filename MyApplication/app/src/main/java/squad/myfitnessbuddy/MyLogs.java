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
    String[] timeOptions = new String[] {"Week", "2 Weeks", "Month", "3 Months", "All Logs"};
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
            //Open the database
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //Open the logs table
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Initialize the containers (Tree sets) for all three filters
        exerciseSet = new TreeSet<>();
        workoutSet = new TreeSet<>();
        bodypartSet = new TreeSet<>();

        //Populate the above three containers with the appropriate information
        populateFilters();

        //Initialize the time spinner (1st on the page)
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> timeSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, timeOptions);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeSpinner.setAdapter(timeSpinnerAdapter);
        timeSpinner.setOnItemSelectedListener(this);

        //Initialize the filterBy spinner (2nd on the page)
        filterBySpinner = (Spinner) findViewById(R.id.filterBySpinner);
        ArrayAdapter<CharSequence> filterByAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, filterByOptions);
        filterByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        filterBySpinner.setAdapter(filterByAdapter);
        filterBySpinner.setOnItemSelectedListener(this);

        //Intialize the filterOptions spinner (3rd on the page)
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

    //Looks at the current position of the time spinner and returns the appropriate query string
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

    //The onClick method for the 'Search' button
    public void onQueryClick(View view) {

        String queryString = "";
        //Retrieve the current selection (Strings as opposed to indices) in filter 2 and filter 3 respectively
        String filterBySelected = filterBySpinner.getSelectedItem().toString();
        String filterOptionsSelected = filterOptionsSpinner.getSelectedItem().toString();

        //If only the time filter is selected, populate the listview with the query for the appropriate timeframe
        if(filterBySelected.equals("") || filterOptionsSelected.equals("")) {
            populateLogs(timeString());
        }

        //The user wants to query with a particular criteria
        else {
            switch(filterBySpinner.getSelectedItemPosition()) {
                //The criteria selected by the user is an 'Exercise'. So query the logs table with the selected exercise name.
                case 1:
                    queryString = timeString() + " AND exercise = '" + filterOptionsSelected + "'";
                    break;
                //The criteria selected by the user is a 'Workout'. So query the logs table with the selected workout name.
                case 2:
                    queryString = timeString() + " AND workout = '" + filterOptionsSelected + "'";
                    break;
                //The criteria selected by the user is a 'Body Part'. So query the logs table with the selected body part.
                case 3:
                    queryString = "SELECT * FROM logs LEFT OUTER JOIN exercises ON exercises.name = logs.exercise WHERE exercises.primaryWorked = '" + filterOptionsSelected +  "'";
            }
            //Populate the listview with the appopriate search
            populateLogs(queryString);
        }
    }

    //Set the 3rd filter (filterOptions) to the appropriate type based on the user's selection for the 2nd filter (filterBy)
    //For example, if the user selected 'Exercise' in filter 2 (filterBy), populate filter 3 (filterOptions) with a list of exercises.
    protected void setFilterOptions(String[] array) {
        ArrayAdapter<CharSequence> filterOptionsAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, array);
        filterOptionsSpinner.setAdapter(filterOptionsAdapter);
        filterOptionsSpinner.setOnItemSelectedListener(this);
    }

    //The method that actually queries the database with the queryString passed from the search button's onClick method.
    protected void populateLogs(String queryString) {

        try {
            String workoutName;
            String dateString;
            String exerciseName;
            int reps;
            int weight;

            //Create an adapter for the listview
            CustomAdapter mAdapter = new CustomAdapter(this);
            this.listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(mAdapter);

            //Create a query with the queryString passed into the function
            Cursor c = exerciseDB.rawQuery(queryString, null);

            //If no results are found for the query, display an information message
            if(c.getCount() == 0) {
                mAdapter.addItem("No results found.");
            }

            //1 or more results have been found for the query
            else {
                int dateColumn = c.getColumnIndex("date");
                int workoutColumn = c.getColumnIndex("workout");
                int exerciseColumn = c.getColumnIndex("exercise");
                int repsColumn = c.getColumnIndex("reps");
                int weightColumn = c.getColumnIndex("weight");

                //Holds the exercises with the same workout name so that they can be displayed together
                ArrayList<Exercise> arrayList = new ArrayList<>();
                Exercise current, previous = null;

                //Traverse the logs table backwards
                for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {

                    workoutName = c.getString(workoutColumn);
                    dateString = formatDate(c.getString(dateColumn));
                    exerciseName = c.getString(exerciseColumn);
                    reps = c.getInt(repsColumn);
                    weight = c.getInt(weightColumn);

                    //This is the first set we have encountered. For all other sets, the else block will be called.
                    if (previous == null) {
                        current = new Exercise(workoutName, exerciseName, reps, weight, dateString);
                        arrayList.add(current);
                        previous = current;
                    }

                    else {
                        current = new Exercise(workoutName, exerciseName, reps, weight, dateString);

                        //If this set has the same workoutName and Date as the previous one, add it to the collection
                        if (current.workoutName.equals(previous.workoutName) && current.date.equals(previous.date)) {
                            arrayList.add(current);
                        }
                        //This set is part of a different workout or has a different date
                        else {
                            //Print out whatever the collection is holding at this point and clear it out
                            printArrayList(arrayList, mAdapter);
                            arrayList.clear();
                            //Repeat the same procedure starting with this set
                            arrayList.add(current);
                            previous = current;
                        }
                    }
                }
                //At the end, if there are sets in the collection, dump those to the listview too
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

    //Method that prints each workout's information in the listview
    public void printArrayList(ArrayList<Exercise> arrayList, CustomAdapter mAdapter) {
        mAdapter.addSectionHeaderItem(arrayList.get(0).workoutName + " : " + arrayList.get(0).date);
        for(Exercise e : arrayList) {
            mAdapter.addItem(e.exerciseName + "   " + e.reps + " x " + e.weight + " lbs");
        }
    }

    //Method to populate the 3 filters based on what's in the database
    //All exercises are displayed
    //Only workouts that exist in the logs table are displayed
    //All bodyparts are displayed
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

    //Convert the date to a format suitable for the listview
    public String formatDate(String dateString){
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yy-MM-dd");
        String result = "";

        try {
           result =  newFormat.format(oldFormat.parse(dateString));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Change the 3rd filter based upon the user's selection of the 2nd filter
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

//Adapter necessary for the listview display
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

//Class exercise that makes collecting the attributes in a collection easier (ArrayList<Exercise>)
//done separate to include date
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

