package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

public class Setup extends AppCompatActivity {

    EditText firstNameET, lastNameET, ageET, heightET, weightET;
    RadioGroup genderRG;
    RadioButton maleRB, femaleRB;

    int age;
    float[] metabolicRates;
    String firstName, lastName, gender;
    float height, weight, bmi, sedentary, lightlyActive, moderatelyActive, veryActive, extremelyActive;

    SharedPreferences sharedPreference;

    //Function that retrieves user information and save it to the package's shared preference
    public void saveSetUp(View view) {

        EditText[] editTextArray = {firstNameET, lastNameET, ageET, heightET, weightET};

        String[] errorMessages = {
                "First name cannot be empty!",
                "Last name cannot be empty!",
                "Age cannot be empty!",
                "Height cannot be empty!",
                "Weight cannot be empty!"
        };

        if (isEmpty(editTextArray, errorMessages)) return;

        //Checks whether user has left both 'Male' and 'Female' empty
        if (genderRG.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "You must select a gender!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (maleRB.isChecked()) gender = "Male";
        else gender = "Female";

        //Retrieve user first name, last name, age, height, weight and store them into appropriate types
        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        age = Integer.parseInt(ageET.getText().toString());
        height = Float.parseFloat(heightET.getText().toString());
        weight = Float.parseFloat(weightET.getText().toString());

        //Call the functions to calculate BMI and Metabolic Rates
        bmi = calculateBMI(height, weight);
        metabolicRates = calculateMetabolicRates(gender, (float) age, height, weight);

        //Stores the metabolic rates in respective variables
        sedentary = metabolicRates[0];
        lightlyActive = metabolicRates[1];
        moderatelyActive = metabolicRates[2];
        veryActive = metabolicRates[3];
        extremelyActive = metabolicRates[4];

        //Create a sharedPreference editor
        SharedPreferences.Editor editor = sharedPreference.edit();

        //Use the editor to store all the values in the SharedPreference
        editor.putString(ConstantValues.cSP_FIRST_NAME, firstName);
        editor.putString(ConstantValues.cSP_LAST_NAME, lastName);
        editor.putInt(ConstantValues.cSP_AGE, age);
        editor.putFloat(ConstantValues.cSP_HEIGHT, height);
        editor.putFloat(ConstantValues.cSP_WEIGHT, weight);
        editor.putString(ConstantValues.cSP_GENDER, gender);
        editor.putFloat(ConstantValues.cSP_BMI, bmi);
        editor.putFloat(ConstantValues.cSP_SEDENTARY, sedentary);
        editor.putFloat(ConstantValues.cSP_LIGHTLY_ACTIVE, lightlyActive);
        editor.putFloat(ConstantValues.cSP_MODERATELY_ACTIVE, moderatelyActive);
        editor.putFloat(ConstantValues.cSP_VERY_ACTIVE, veryActive);
        editor.putFloat(ConstantValues.cSP_EXTREMELY_ACTIVE, extremelyActive);
        editor.apply();

        //After saving the data, we are ready to go to the user profile
        UserProfile.backButtonCheck("setupPage");
        goToUserProfile();
    }

    //Calculates BMI based on height and weight
    private float calculateBMI(float height, float weight) {
        return Math.round((703 * (weight / (float) Math.pow((double) height, 2))) * 100.0f) / 100.0f;
    }

    //Calculates the metabolic rates of the user for different activity level and return in an array of floats
    private float[] calculateMetabolicRates(String gender, float age, float height, float weight) {

        float sedentary, lightlyActive, moderatelyActive, veryActive, extremelyActive, base;

        if (gender.equals("Male")) {
            base = 66 + (6.23f * weight) + (12.7f * height) - (6.8f * age);

        } else if (gender.equals("Female")) {
            base = 655 + (4.35f * weight) + (4.7f * height) - (4.7f * age);

        }
        //if gender isn't male or female, we have a software problem, return 0's so developer knows to fix
        else {
            base = 0;
        }

        sedentary = Math.round(base * 1.1f);
        lightlyActive = Math.round(base * 1.275f);
        moderatelyActive = Math.round(base * 1.35f);
        veryActive = Math.round(base * 1.525f);
        extremelyActive = Math.round(base * 1.75f);
        float[] rates = {sedentary, lightlyActive, moderatelyActive, veryActive, extremelyActive};
        return rates;
    }

    private boolean isEmpty(EditText[] editTextArray, String[] errorMessages) {

        for (int i = 0; i < editTextArray.length; i++) {
            if (editTextArray[i].getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), errorMessages[i], Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
      //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000"))); // set your desired color
        actionBar.setTitle("Setup Page");

        //create the database manager
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        //create database if it does not exist (or was erased) for initial exercises
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        //Matches each of the textfields, edittexts, radiobuttons and radiogroup to the respective local variables
        firstNameET = (EditText) findViewById(R.id.userFirstName);
        lastNameET = (EditText) findViewById(R.id.userLastName);
        ageET = (EditText) findViewById(R.id.userAge);
        heightET = (EditText) findViewById(R.id.userHeight);
        weightET = (EditText) findViewById(R.id.userWeight);
        genderRG = (RadioGroup) findViewById(R.id.genderRadioGroup);
        maleRB = (RadioButton) findViewById(R.id.male);
        femaleRB = (RadioButton) findViewById(R.id.female);

        //Here we instantiate the sharedPreference by giving it the package name
        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        //Checks whether  if we came from another class
        Intent intent = getIntent();

        //If intent.getExtras() is not null,  we got called from another class
        if (intent.getExtras() != null) {

            //We came from another class -> User wants to edit the information -> Let's preload the information in the boxes for the user to edit
            firstNameET.setText(sharedPreference.getString(ConstantValues.cSP_FIRST_NAME, ""));
            lastNameET.setText(sharedPreference.getString(ConstantValues.cSP_LAST_NAME, ""));
            ageET.setText(Integer.toString(sharedPreference.getInt(ConstantValues.cSP_AGE, 0)));
            heightET.setText(Float.toString(sharedPreference.getFloat(ConstantValues.cSP_HEIGHT, 0)));
            weightET.setText(Float.toString(sharedPreference.getFloat(ConstantValues.cSP_WEIGHT, 0)));

            if (sharedPreference.getString(ConstantValues.cSP_GENDER, "").equals("Male")) {
                genderRG.check(R.id.male);
            } else {
                genderRG.check(R.id.female);
            }
        }

        //We did not get called from another class -> User didn't select to edit the information -> Check whether user has set the info before -> If so go to the user profile -> Otherwise, stay on the page
        else {

            String tempFirstName = sharedPreference.getString(ConstantValues.cSP_FIRST_NAME, "");
            if (!tempFirstName.equals("")) {
                goToUserProfile();
            }
        }
    }

    //Transfer to the user profile page
    public void goToUserProfile() {
        Intent profile = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(profile);
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