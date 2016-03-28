package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserProfile extends AppCompatActivity {

    TextView fullNameTV, ageTV, genderTV, heightTV, weightTV, bmiTV, sedentaryTV, lightlyActiveTV, moderatelyActiveTV, veryActiveTV, extremelyActiveTV;
    SharedPreferences sharedPreference;
    LinearLayout linearLayout, popUpLayout;


    //The user clicked the 'Edit User Profile' Button -> Transfer back to the SetUp page
    public void editProfileOnClick(View view){
        Intent intent = new Intent(getApplicationContext(), Setup.class);
        intent.putExtra("fromUserProfile", true);
        startActivity(intent);
    }

    //The user clicked the back button on the popup -> Hide the popup
    public void backOnClick(View view) {
        hidePopUp();
    }

    //The user clicked 'Menu' button -> If popup is showing, hide it & vice-versa
    public void menuOnClick(View view) {
        if(popUpLayout.getVisibility() == View.INVISIBLE) {
           showPopUp();
        }
        else {
            hidePopUp();
        }
    }

    //Method that adjusts the visibility of the background layouts to show the popup
    public void showPopUp() {
        linearLayout.setVisibility(View.INVISIBLE);
        fullNameTV.setVisibility(View.INVISIBLE);
        popUpLayout.setVisibility(View.VISIBLE);
    }

    //Method that adjusts the visibility of the background layouts to hide the popup
    public void hidePopUp() {
        popUpLayout.setVisibility(View.INVISIBLE);
        fullNameTV.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String heightStr, weightStr;

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        popUpLayout = (LinearLayout) findViewById(R.id.popUpLayout);

        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        fullNameTV          = (TextView) findViewById(R.id.fullNameTV);
        ageTV               = (TextView) findViewById(R.id.ageTV);
        genderTV            = (TextView) findViewById(R.id.genderTV);
        heightTV            = (TextView) findViewById(R.id.heightTV);
        weightTV            = (TextView) findViewById(R.id.weightTV);
        bmiTV               = (TextView) findViewById(R.id.bmiTV);
        sedentaryTV         = (TextView) findViewById(R.id.sedentaryTV);
        lightlyActiveTV     = (TextView) findViewById(R.id.lightlyActiveTV);
        moderatelyActiveTV  = (TextView) findViewById(R.id.moderatelyActiveTV);
        veryActiveTV        = (TextView) findViewById(R.id.veryActiveTV);
        extremelyActiveTV   = (TextView) findViewById(R.id.extremelyActiveTV);

        Intent intent = getIntent();

        float heightFlt = intent.getFloatExtra("Height", 0);
        float weightFlt = intent.getFloatExtra("Weight", 0);

        //format height and weight to show appropriate precision per user input
        if (heightFlt == Math.floor(heightFlt))
        {
            heightStr = (String.format("%.0f",heightFlt));
        }
        else
        {
            heightStr = (String.format("%.2f",heightFlt));
        }
        //To delete
        if (weightFlt == Math.floor(weightFlt))
        {
            weightStr = (String.format("%.0f",weightFlt));
        }
        else
        {
            weightStr = (String.format("%.2f",weightFlt));
        }


        fullNameTV.setText(intent.getStringExtra("First Name") + " " +  intent.getStringExtra("Last Name"));
        ageTV.setText(fixedLengthString("Age", 15) + String.valueOf(intent.getIntExtra("Age", 0)));
        genderTV.setText(fixedLengthString("Gender", 12) + intent.getStringExtra("Gender"));
        heightTV.setText(fixedLengthString("Height", 13) + heightStr + "\"");
        weightTV.setText(fixedLengthString("Weight", 12)  + weightStr + " lb");
        bmiTV.setText(fixedLengthString("BMI", 14) + String.valueOf(intent.getFloatExtra("BMI", 0)));

        sedentaryTV.setText(fixedLengthString("Sedentary", 30) + String.format("%.0f", intent.getFloatExtra("Sedentary", 0)));
        lightlyActiveTV.setText(fixedLengthString("Lightly Active",29) + String.format("%.0f", intent.getFloatExtra("Lightly Active", 0)));
        moderatelyActiveTV.setText(fixedLengthString("Moderately Active",24) + String.format("%.0f", intent.getFloatExtra("Moderately Active", 0)));
        veryActiveTV.setText("Very Active                   " + String.format("%.0f", intent.getFloatExtra("Very Active", 0)));
        extremelyActiveTV.setText(fixedLengthString("Extremely Active", 25) + String.format("%.0f", intent.getFloatExtra("Extremely Active", 0)));


        // For testing the first page multiple times. When applied, the sharedPreference data is deleted and the user goes to the Set Up page when the application starts next time.
        // sharedPreference.edit().clear().apply();

    }

    //Represent a string in 'length' spaces
    public static String fixedLengthString(String string, int length) {
        return String.format("%1$" + (0-length) + "s", string);
    }
}
