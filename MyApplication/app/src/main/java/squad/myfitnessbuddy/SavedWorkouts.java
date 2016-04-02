package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SavedWorkouts extends MenuButtonBar {
    LinearLayout createWorkoutPopup;

    //This will let you choose between create or predefined, for now just goes to create page
    public void addWorkoutOnClick(View view)
    {
        createWorkoutPopup.setVisibility(View.VISIBLE);
    }

    public void customizedWorkoutButton (View view){
        Intent addWorkout = new Intent(getApplicationContext(), CreateWorkout.class);
        startActivity(addWorkout);
    }

    public void PredefinedWorkoutOnClick(View view) {
        Intent predefinedWorkout = new Intent(getApplicationContext(), PredefinedWorkouts.class);
        startActivity(predefinedWorkout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Saved Workouts");

        createWorkoutPopup = (LinearLayout) findViewById(R.id.createWorkoutLayout);

    }




}
