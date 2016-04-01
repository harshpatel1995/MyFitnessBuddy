package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SavedWorkouts extends AppCompatActivity {

    //This will let you choose between create or predefined, for now just goes to create page
    public void addWorkoutOnClick(View view)
    {
        Intent addWorkout = new Intent(getApplicationContext(), CreateWorkout.class);
        startActivity(addWorkout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Saved Workouts");
        assert actionBar != null;

    }




}
