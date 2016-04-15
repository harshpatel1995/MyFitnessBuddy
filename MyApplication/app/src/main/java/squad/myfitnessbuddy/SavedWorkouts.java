package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedWorkouts extends MenuButtonBar {
    SharedPreferences sharedPreference;
    LinearLayout createWorkoutPopup, deleteWorkoutPopup, menuButtons, mainButtonBar, mainSnacks, mainShaders;
    public ListView savedWorkOutsLV;
    public Button addWorkoutButtonBT;

    //database for system
    SQLiteDatabase database;
    //listview control that displays exercises on screen


    //This will let you choose between create or predefined, for now just goes to create page
    public void addWorkoutOnClick(View view) {
        createWorkoutPopup.setVisibility(View.VISIBLE);
        menuButtons.setVisibility(View.INVISIBLE);
        savedWorkOutsLV.setVisibility(View.INVISIBLE);
        addWorkoutButtonBT.setVisibility(View.INVISIBLE);
        mainButtonBar.setVisibility(View.INVISIBLE);
        mainSnacks.setVisibility(View.INVISIBLE);
        mainShaders.setVisibility(View.INVISIBLE);
    }

    public void customizedWorkoutButton (View view){
        Intent addWorkout = new Intent(getApplicationContext(), CreateWorkout.class);
        startActivity(addWorkout);
    }

    public void PredefinedWorkoutOnClick(View view) {
        Intent predefinedWorkout = new Intent(getApplicationContext(), PredefinedWorkouts.class);
        startActivity(predefinedWorkout);
    }

    public void backOnClick(View view) {
        createWorkoutPopup.setVisibility(View.INVISIBLE);
        menuButtons.setVisibility(View.VISIBLE);
        savedWorkOutsLV.setVisibility(View.VISIBLE);
        addWorkoutButtonBT.setVisibility(View.VISIBLE);
        mainButtonBar.setVisibility(View.VISIBLE);
        mainSnacks.setVisibility(View.VISIBLE);
        mainShaders.setVisibility(View.VISIBLE);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workouts);

        workoutsButtonTV = (TextView) findViewById(R.id.workoutsButtonBarText);
        workoutsButtonIV = (ImageView) findViewById(R.id.workoutsButtonBarIcon);
        buttonBarL = (LinearLayout) findViewById(R.id.workoutsButtonBar);
        buttonBarSnackL = (TextView) findViewById(R.id.workoutsButtonBarSnack);
        buttonBarShaderL = (TextView) findViewById(R.id.workoutsButtonBarShader);

        workoutsButtonTV.setTextColor(ContextCompat.getColor(this, R.color.windowBackground));
        buttonBarL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonPressed));
        buttonBarSnackL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBarSnackPressed));
        buttonBarShaderL.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBarShaderPressed));

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            workoutsButtonIV.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.workout_button_active));
        } else {
            workoutsButtonIV.setBackground(ContextCompat.getDrawable(this, R.drawable.workout_button_active));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Here we instantiate the sharedPreference by giving it the package name
        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Saved Workouts");

        try {
            database = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            //open or create database
            database.execSQL(ConstantValues.cCREATE_OR_OPEN_SAVED_WORKOUTS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        addWorkoutButtonBT = (Button) findViewById(R.id.addWorkoutButton);
        createWorkoutPopup = (LinearLayout) findViewById(R.id.createWorkoutLayout);
        deleteWorkoutPopup = (LinearLayout) findViewById(R.id.savedWorkoutDeleteWorkoutLinearLayout);
        menuButtons        = (LinearLayout) findViewById(R.id.savedWorkoutStartMenuLinearLayout);
        mainButtonBar      = (LinearLayout) findViewById(R.id.buttonBarMenu);
        mainSnacks         = (LinearLayout) findViewById(R.id.buttonBarSnacks);
        mainShaders         = (LinearLayout) findViewById(R.id.buttonBarShaders);

        savedWorkOutsLV = (ListView) findViewById(R.id.savedWorkoutsLV);
        //populate list of exercises
        populateExercises();

    }
    //Method to populate list of exercises
    protected void populateExercises() {
        //create array to store exercise names
        final ArrayList<String> workoutsList = new ArrayList<>();

        if (database != null) {
        try {
                //set cursor to start traversing search
                //raw query is SQL code (Select everything from table "exercises" and order them by name)
                Cursor c = database.rawQuery("SELECT * FROM savedWorkouts ORDER BY name", null);

                //get items from "name" column of table
                int nameIndex = c.getColumnIndex("name");

                //add exercise to list and move cursor to next exercise
                 while (c != null &&  c.moveToNext()) {
                    workoutsList.add(c.getString(nameIndex));
                }
            c.close();

            }catch(Exception e){
                e.printStackTrace();
            }

            //adapter to get list into ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, workoutsList);
            savedWorkOutsLV.setAdapter(adapter);
            //check one workout at a time
            savedWorkOutsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }
        else{
            //message for developer
            Log.i("Error", "Database is null.");
        }
    }

    //asks user if he or she really wants to delete the workout
    public void displayDeleteOption(View view){

        String workoutToDeleteStr = getCheckedItemName(savedWorkOutsLV);
        if (!workoutToDeleteStr.equals("")) {

            String deleteWorkoutText = "Are you sure you want to delete workout:\n\"" + workoutToDeleteStr + "?\"";
            TextView deleteMessage = (TextView) findViewById(R.id.savedWorkoutsDeleteMessage);
            deleteMessage.setText(deleteWorkoutText);
            deleteWorkoutPopup.setVisibility(View.VISIBLE);
            menuButtons.setVisibility(View.INVISIBLE);
            savedWorkOutsLV.setEnabled(false);
            mainButtonBar.setVisibility(View.INVISIBLE);
            mainSnacks.setVisibility(View.INVISIBLE);
            mainShaders.setVisibility(View.INVISIBLE);
            addWorkoutButtonBT.setVisibility(View.INVISIBLE);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please select a workout to delete.",Toast.LENGTH_SHORT).show();
        }
    }

    public void doNotDeleteWorkout(View view){

        deleteWorkoutPopup.setVisibility(View.INVISIBLE);
        menuButtons.setVisibility(View.VISIBLE);
        savedWorkOutsLV.setEnabled(true);
        mainButtonBar.setVisibility(View.VISIBLE);
        mainSnacks.setVisibility(View.VISIBLE);
        mainShaders.setVisibility(View.VISIBLE);
        addWorkoutButtonBT.setVisibility(View.VISIBLE);
    }

    public void deleteWorkout(View view){
        String selectedWorkoutName = getCheckedItemName(savedWorkOutsLV);

        try {
            //adds a new record for the workout
            database.execSQL("DELETE FROM savedWorkouts WHERE name = '" + selectedWorkoutName + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }


        finish();
        //reopen the page to see changes
        Intent savedWorkouts = new Intent(getApplicationContext(), SavedWorkouts.class);
        startActivity(savedWorkouts);

    }

    public void startWorkout(View view){

        String selectedWorkoutName = getCheckedItemName(savedWorkOutsLV);

        if (!selectedWorkoutName.equals("")) {

            SharedPreferences.Editor editor = sharedPreference.edit();

            //Use the editor to store the name of the current workout to preview in the SharedPreference
            editor.putString(ConstantValues.cSP_STARTED_WORKOUT, selectedWorkoutName);
            editor.apply();

            //open workout page
            Intent startWorkout = new Intent(getApplicationContext(), StartWorkout.class);
            startActivity(startWorkout);
        }
        else{
            //if nothing selected, display error message
            Toast.makeText(getApplicationContext(),"Please select a workout to start.",Toast.LENGTH_SHORT).show();
        }

    }

    //goes to the preview workout activity to preview the workout
    public void previewWorkout(View view){
        String selectedWorkoutName = getCheckedItemName(savedWorkOutsLV);

        if (!selectedWorkoutName.equals("")) {

            SharedPreferences.Editor editor = sharedPreference.edit();

            //Use the editor to store the name of the current workout to preview in the SharedPreference
            editor.putString(ConstantValues.cSP_PREVIEW_WORKOUT, selectedWorkoutName);
            editor.putBoolean(ConstantValues.cSP_IS_PREVIEW_FOR_PREDEFINED, false);
            editor.apply();

            //open preview page
            Intent previewWorkout = new Intent(getApplicationContext(), PreviewWorkout.class);
            startActivity(previewWorkout);
        }
        else{
            //if nothing selected, display error message
            Toast.makeText(getApplicationContext(),"Please select a workout to preview.",Toast.LENGTH_SHORT).show();
        }

    }

    //returns the name of the item that is checked in the list.
    //returns empty string if it noting is checked
    public String getCheckedItemName(ListView listView)
    {
        String selectedItem = "";

        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

        //iterate through list
        for(int counter = 0; counter < listView.getCount(); counter++){
            //if something is checked get name of item
            if (sparseBooleanArray.get(counter)){
                selectedItem = listView.getItemAtPosition(counter).toString();
                break;
            }
        }
        return selectedItem;
    }
}
