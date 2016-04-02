package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class SavedWorkouts extends MenuButtonBar {
    SharedPreferences sharedPreference;
    LinearLayout createWorkoutPopup, deleteWorkoutPopup, menuButtons;

    //database for system
    SQLiteDatabase database;
    //listview control that displays exercises on screen
    public ListView savedWorkOutsLV;

    //This will let you choose between create or predefined, for now just goes to create page
    public void addWorkoutOnClick(View view) {
        createWorkoutPopup.setVisibility(View.VISIBLE);
        menuButtons.setVisibility(View.INVISIBLE);
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
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workouts);
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

        createWorkoutPopup = (LinearLayout) findViewById(R.id.createWorkoutLayout);
        deleteWorkoutPopup = (LinearLayout) findViewById(R.id.savedWorkoutDeleteWorkoutLinearLayout);
        menuButtons        = (LinearLayout) findViewById(R.id.savedWorkoutStartMenuLinearLayout);

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

                //move cursor to to of list (table)
                c.moveToFirst();

                //add exercise to list and move cursor to next exercise
                while (c != null) {

                    workoutsList.add(c.getString(nameIndex));
                    c.moveToNext();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            //adapter to get list into ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workoutsList);
            savedWorkOutsLV.setAdapter(adapter);
            //check multiple exercises at a time
            savedWorkOutsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }
        else{
            Log.i("Error", "Database is null.");
        }
    }

    //asks user if he or she really wants to delete the workout
    public void displayDeleteOption(View view){

        String workoutToDeleteStr = getCheckedItemName(savedWorkOutsLV);
        if (!workoutToDeleteStr.equals("")) {

            String deleteWorkoutText = "Are you sure you want to delete workout: \"" + workoutToDeleteStr + "\"?";
            TextView deleteMessage = (TextView) findViewById(R.id.savedWorkoutsDeleteMessage);
            deleteMessage.setText(deleteWorkoutText);
            deleteWorkoutPopup.setVisibility(View.VISIBLE);
            menuButtons.setVisibility(View.INVISIBLE);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please select a workout to delete.",Toast.LENGTH_SHORT).show();
        }
    }

    public void doNotDeleteWorkout(View view){

        deleteWorkoutPopup.setVisibility(View.INVISIBLE);
        menuButtons.setVisibility(View.VISIBLE);
    }

    public void deleteWorkout(View view){

        Toast.makeText(getApplicationContext(),"This feature coming soon.",Toast.LENGTH_SHORT).show();

    }


    public void startWorkout(View view){

        Toast.makeText(getApplicationContext(),"This feature coming soon.",Toast.LENGTH_SHORT).show();
    }

    //goes to the preview workout activity to preview the workout
    public void previewWorkout(View view){
        String selectedWorkoutName = getCheckedItemName(savedWorkOutsLV);

        if (!selectedWorkoutName.equals("")) {

            SharedPreferences.Editor editor = sharedPreference.edit();

            //Use the editor to store the name of the current workout to preview in the SharedPreference
            editor.putString(ConstantValues.cSP_PREVIEW_WORKOUT, selectedWorkoutName);

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
            if (sparseBooleanArray.get(counter) == true){
                selectedItem = listView.getItemAtPosition(counter).toString();
                break;
            }
        }
        return selectedItem;
    }



}
