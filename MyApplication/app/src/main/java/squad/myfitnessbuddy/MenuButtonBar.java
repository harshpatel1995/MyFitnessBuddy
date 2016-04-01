package squad.myfitnessbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuButtonBar extends AppCompatActivity{

    //The user clicked on "View My Logs"
    public void viewLogsOnClick(View view) {
        Intent viewMyLogs = new Intent(getApplicationContext(), MyLogs.class);
        startActivity(viewMyLogs);
    }

    //The user clicked on 'My Saved Workouts' from the menu options
    public void savedWorkoutsOnClick(View view) {
        Intent savedWorkout = new Intent(getApplicationContext(), SavedWorkouts.class);
        startActivity(savedWorkout);
    }

    public void goToProfileOnClick(View view) {
        Intent profile = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(profile);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_button_bar);
    }
}
