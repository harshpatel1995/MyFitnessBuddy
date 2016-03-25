package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.math.BigDecimal;

public class Setup extends AppCompatActivity {

    EditText firstNameET, lastNameET, ageET, heightET, weightET;
    RadioGroup genderRG;
    RadioButton maleRB,femaleRB;

    String firstName, lastName, gender;
    int age;
    double height, weight;

    SharedPreferences sharedPreference;

    public void saveSetUp(View view) {
        EditText[] editTextArray = {firstNameET, lastNameET, ageET, heightET, weightET};

        String[] errorMessages = {
                "First name cannot be empty!",
                "Last name cannot be empty!",
                "Age cannot be empty!",
                "Height cannot be empty!",
                "Weight cannot be empty!"
        };

        //craptest4

        if(isEmpty(editTextArray,errorMessages)) return;

        firstName  =  firstNameET.getText().toString();
        lastName   =  lastNameET.getText().toString();
        age        =  Integer.parseInt(ageET.getText().toString());
        height     =  Double.parseDouble(heightET.getText().toString());
        weight     =  Double.parseDouble(weightET.getText().toString());

        if (genderRG.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "You must select a gender!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(maleRB.isChecked())  gender = "Male" ;
        else                    gender = "Female";


        sharedPreference.edit().putString("First Name", firstName).apply();
        sharedPreference.edit().putString("Last Name", lastName).apply();
        sharedPreference.edit().putInt("Age", age).apply();
        sharedPreference.edit().putFloat("Height", (float)height).apply();
        sharedPreference.edit().putFloat("Weight", (float) weight).apply();
        sharedPreference.edit().putString("Gender", gender).apply();

        goToUserProfile();

    }

    private boolean isEmpty(EditText[] editTextArray, String[] errorMessages) {

        for(int i = 0; i < editTextArray.length; i++) {
            if (editTextArray[i].getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), errorMessages[i] , Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);
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

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000"))); // set your desired color
        actionBar.setTitle("Setup Page");

        firstNameET = (EditText) findViewById(R.id.userFirstName);
        lastNameET  = (EditText) findViewById(R.id.userLastName);
        ageET       = (EditText) findViewById(R.id.userAge);
        heightET    = (EditText) findViewById(R.id.userHeight);
        weightET    = (EditText) findViewById(R.id.userWeight);

        genderRG  = (RadioGroup) findViewById(R.id.genderRadioGroup);

        maleRB   = (RadioButton) findViewById(R.id.male);
        femaleRB = (RadioButton) findViewById(R.id.female);

        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        Log.i("H", String.valueOf((double) sharedPreference.getFloat("Height",0)));
        Log.i("W", String.valueOf((double) sharedPreference.getFloat("Weight",0)));

        String tempFirstName = sharedPreference.getString("First Name", "");
        if(tempFirstName != "") {
           goToUserProfile();
        }

    }

    //Transfer to the user profile page
    public void goToUserProfile() {

        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        intent.putExtra("First Name", sharedPreference.getString("First Name", ""));
        intent.putExtra("Last Name",  sharedPreference.getString("Last Name", ""));
        intent.putExtra("Gender", sharedPreference.getString("Gender", ""));
        intent.putExtra("Age", sharedPreference.getInt("Age", 0));
        intent.putExtra("Height", (double) sharedPreference.getFloat("Height", 0));
        Log.i("Height", BigDecimal.valueOf((double) sharedPreference.getFloat("Height", 0)).toPlainString());
        intent.putExtra("Weight", (double) sharedPreference.getFloat("Weight", 0));
        Log.i("Weight", String.valueOf((double) sharedPreference.getFloat("Weight", 0)));

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
