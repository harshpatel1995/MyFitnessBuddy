package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserProfile extends AppCompatActivity {

    TextView firstNameTV, lastNameTV, ageTV, genderTV, heightTV, weightTV, bmiTV,
        sedentaryTV, lightlyActiveTV, moderatelyActiveTV, veryActiveTV, extremelyActiveTV;
    SharedPreferences sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String ageStr, genderStr, heightStr, weightStr, bmiStr,
                sedentaryStr, lightlyActiveStr, moderatelyActiveStr, veryActiveStr, extremelyActiveStr;


        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        firstNameTV         = (TextView) findViewById(R.id.profileFirstName);
        lastNameTV          = (TextView) findViewById(R.id.profileLastName);
        ageTV               = (TextView) findViewById(R.id.profileAge);
        genderTV            = (TextView) findViewById(R.id.profileGender);
        heightTV            = (TextView) findViewById(R.id.profileHeight);
        weightTV            = (TextView) findViewById(R.id.profileWeight);
        bmiTV               = (TextView) findViewById(R.id.profileBMI);
        sedentaryTV         = (TextView) findViewById(R.id.profileSedentary);
        lightlyActiveTV     = (TextView) findViewById(R.id.profileLightlyActive);
        moderatelyActiveTV  = (TextView) findViewById(R.id.profileModeratelyActive);
        veryActiveTV        = (TextView) findViewById(R.id.profileVeryActive);
        extremelyActiveTV   = (TextView) findViewById(R.id.profileExtremelyActive);

        Intent intent = getIntent();

        ageStr = ("Age: " + String.valueOf(intent.getIntExtra("Age", 0)));
        bmiStr = ("BMI: " + String.valueOf(intent.getFloatExtra("BMI", 0)));
        sedentaryStr = ("Sedentary:                    " + String.format("%.0f", intent.getFloatExtra("Sedentary", 0)));
        lightlyActiveStr = ("Lightly Active:              " + String.format("%.0f", intent.getFloatExtra("Lightly Active", 0)));
        moderatelyActiveStr = ("Moderately Active:      " + String.format("%.0f", intent.getFloatExtra("Moderately Active", 0)));
        veryActiveStr = ("Very Active:                  " + String.format("%.0f", intent.getFloatExtra("Very Active", 0)));
        extremelyActiveStr = ("Extremely Active:         " + String.format("%.0f", intent.getFloatExtra("Extremely Active", 0)));

        float heightFlt = intent.getFloatExtra("Height", 0);
        float weightFlt = intent.getFloatExtra("Weight", 0);

        //format height and weight to show appropriate precision per user input
        if (heightFlt == Math.floor(heightFlt))
        {
            heightStr = ("Height: " + String.format("%.0f",heightFlt) + " in.");
        }
        else
        {
            heightStr = ("Height: " + String.format("%.2f",heightFlt) + " in.");
        }
        //To delete
        if (weightFlt == Math.floor(weightFlt))
        {
            weightStr = ("Weight: " + String.format("%.0f",weightFlt) + " lb.");
        }
        else
        {
            weightStr = ("Weight: " + String.format("%.2f",weightFlt) + " lb.");
        }


        firstNameTV.setText(intent.getStringExtra("First Name"));
        lastNameTV.setText(intent.getStringExtra("Last Name"));
        genderTV.setText(intent.getStringExtra("Gender"));

        ageTV.setText(ageStr);
        heightTV.setText(heightStr);
        weightTV.setText(weightStr);
        bmiTV.setText(bmiStr);
        sedentaryTV.setText(sedentaryStr);
        lightlyActiveTV.setText(lightlyActiveStr);
        moderatelyActiveTV.setText(moderatelyActiveStr);
        veryActiveTV.setText(veryActiveStr);
        extremelyActiveTV.setText(extremelyActiveStr);

        // For testing the first page multiple times. When applied, the sharedPreference data is deleted and the user goes to the Set Up page when the application starts next time.
        // sharedPreference.edit().clear().apply();

    }



}
