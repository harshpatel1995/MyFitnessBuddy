package squad.myfitnessbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuButtonBar extends AppCompatActivity{

    TextView logsButtonTV, workoutsButtonTV, profileButtonTV, buttonBarSnackL, buttonBarShaderL;
    ImageView logsButtonIV, workoutsButtonIV, profileButtonIV;
    LinearLayout buttonBarL;

    //The user clicked on "View My Logs"
    public void viewLogsOnClick(View view) {
        Intent viewMyLogs = new Intent(this, MyLogs.class);
        viewMyLogs.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(viewMyLogs);
    }

    //The user clicked on 'My Saved Workouts' from the menu options
    public void viewWorkoutsOnClick(View view) {
        Intent workouts = new Intent(this, SavedWorkouts.class);
        workouts.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(workouts);
    }

    public void viewProfileOnClick(View view) {
        UserProfile.backButtonCheck("other");
        Intent profile = new Intent(this, UserProfile.class);
        profile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(profile);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_button_bar);
    }

    //TODO fix screen flicker black when transitions are way too quick
}
