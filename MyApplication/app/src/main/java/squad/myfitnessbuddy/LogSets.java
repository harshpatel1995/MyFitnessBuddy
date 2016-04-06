package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LogSets extends AppCompatActivity {

    private ListView addSetsLV;
    private TextView exerciseNameTV;
    private EditText repET, weightET;
    private String exerciseNameStr, workoutNameStr;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreference;

    private final ArrayList<ExerciseSet> setsForCurrentExerciseList = new ArrayList<>();

    public void onClickAddSet(View view){
        repET = (EditText) findViewById(R.id.repET);
        weightET = (EditText) findViewById(R.id.weightET);
        //EditText[] editTextArray = {repET, weightET};

        String[] errorMessagesArr = {
                "Reps cannot be empty!",
                "Weight cannot be empty!",
                "Reps and Weight cannot be empty!"
        };


        String repsStr = repET.getText().toString();
        String weightStr = weightET.getText().toString();

        if ((!repsStr.equals( "")) && (!weightStr.equals(""))) {

            int repsInt = Integer.parseInt(repsStr);
            int weightInt = Integer.parseInt(weightStr);

            ExerciseSet setObj = new ExerciseSet(workoutNameStr, exerciseNameStr, repsInt, weightInt);
            setsForCurrentExerciseList.add(setObj);

            strArr.add("Reps: " + Integer.toString(repsInt) + " Weights: " + Integer.toString(weightInt));
            adapter.notifyDataSetChanged();

            repET.setText("");
            weightET.setText("");

            repET.requestFocus();
        }
        else if ((repsStr + weightStr).equals("")){
            Toast.makeText(getApplicationContext(),errorMessagesArr[2],Toast.LENGTH_SHORT).show();
        }
        else if (repsStr.equals("")){
            Toast.makeText(getApplicationContext(),errorMessagesArr[0],Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),errorMessagesArr[1],Toast.LENGTH_SHORT).show();
        }

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_sets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Log Workout");

        addSetsLV = (ListView) findViewById(R.id.addedSetsLV);
        repET = (EditText) findViewById(R.id.repET);
        weightET = (EditText) findViewById(R.id.weightET);
        exerciseNameTV = (TextView) findViewById(R.id.exerciseNameTV);


        //Here we instantiate the sharedPreference by giving it the package name
        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        exerciseNameStr = sharedPreference.getString(ConstantValues.cSP_CURRENT_EXERCISE_TO_LOG, "Exercise");
        workoutNameStr = sharedPreference.getString(ConstantValues.cSP_STARTED_WORKOUT, "Workout");

        exerciseNameTV.setText(exerciseNameStr);

        strArr = new ArrayList<>();

        adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strArr);
        addSetsLV.setAdapter(adapter);

    }

    //this saves the sets to the workout and then goes back to workout page
    public void saveSetsForExerciseOnClick(View view){

        for (ExerciseSet setObj : setsForCurrentExerciseList){

            StartWorkout.workoutAsListOfSetsList.add(setObj);
            finish();
        }

    }

    //when back or cancel button is clicked, go back to previous activity
    public void backOrCancelOnClick (View view){

        finish();
    }



}
