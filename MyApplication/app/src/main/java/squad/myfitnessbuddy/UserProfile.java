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

    TextView firstNameTV, lastNameTV, ageTV, genderTV, heightTV, weightTV;
    SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        firstNameTV  = (TextView) findViewById(R.id.profileFirstName);
        lastNameTV   = (TextView) findViewById(R.id.profileLastName);
        ageTV        = (TextView) findViewById(R.id.profileAge);
        genderTV     = (TextView) findViewById(R.id.profileGender);
        heightTV     = (TextView) findViewById(R.id.profileHeight);
        weightTV     = (TextView) findViewById(R.id.profileWeight);

        Intent intent = getIntent();

        firstNameTV.setText(intent.getStringExtra("First Name"));
        lastNameTV.setText(intent.getStringExtra("Last Name"));
        genderTV.setText(intent.getStringExtra("Gender"));

        ageTV.setText(String.valueOf(intent.getIntExtra("Age", 0)));
        heightTV.setText(String.valueOf(intent.getDoubleExtra("Height", 0)));
        weightTV.setText(String.valueOf(intent.getDoubleExtra("Weight", 0)));

        // For testing the first page multiple times. When applied, the sharedPreference data is deleted and the user goes to the Set Up page when the application starts next time.
        // sharedPreference.edit().clear().apply();

    }



}
