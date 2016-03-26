package squad.myfitnessbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
    float[] metabolicRates;
    float height, weight, bmi,sedentary,lightlyActive,moderatelyActive,veryActive,extremelyActive;

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

        if(isEmpty(editTextArray,errorMessages)) return;

        if (genderRG.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "You must select a gender!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(maleRB.isChecked()) gender = "Male";
        else                    gender = "Female";

        firstName  =  firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        age        =  Integer.parseInt(ageET.getText().toString());
        height     =  Float.parseFloat(heightET.getText().toString());
        weight     =  Float.parseFloat(weightET.getText().toString());
        bmi        =  calculateBMI(height, weight);
        metabolicRates = calculateMetabolicRates(gender, (float) age, height, weight);
        sedentary =  metabolicRates[0];
        lightlyActive = metabolicRates[1];
        moderatelyActive = metabolicRates[2];
        veryActive = metabolicRates[3];
        extremelyActive = metabolicRates[4];


        sharedPreference.edit().putString("First Name", firstName).apply();
        sharedPreference.edit().putString("Last Name", lastName).apply();
        sharedPreference.edit().putInt("Age", age).apply();
        sharedPreference.edit().putFloat("Height", height).apply();
        sharedPreference.edit().putFloat("Weight",  weight).apply();
        sharedPreference.edit().putString("Gender", gender).apply();
        sharedPreference.edit().putFloat("BMI", bmi).apply();
        sharedPreference.edit().putFloat("Sedentary", sedentary).apply();
        sharedPreference.edit().putFloat("Lightly Active", lightlyActive).apply();
        sharedPreference.edit().putFloat("Moderately Active", moderatelyActive).apply();
        sharedPreference.edit().putFloat("Very Active", moderatelyActive).apply();
        sharedPreference.edit().putFloat("Extremely Active", extremelyActive).apply();

        goToUserProfile();

    }

    //Calculates BMI based on height and weight
    private float calculateBMI (float height, float weight)
    {
        return Math.round((703*(weight/ (float) Math.pow((double)height,2)))*100.0f)/100.0f;
    }

    //Calculates the metabolic rates of the user for different activity level and return in an array of floats
    private float[] calculateMetabolicRates(String gender, float age, float height, float weight)
    {

        float sedentary,lightlyActive,moderatelyActive,veryActive,extremelyActive, base;

        if (gender.equals("Male"))
        {
             base = 66+(6.23f * weight)+(12.7f * height)-(6.8f * age);

        }
        else if (gender.equals("Female"))
        {
            base = 655+(4.35f * weight)+(4.7f * height)-(4.7f * age);

        }
        //if gender isn't male or female, we have a software problem, return 0's so developer knows to fix
        else
        {
            base = 0;
        }

        sedentary = Math.round(base * 1.1f);
        lightlyActive = Math.round(base * 1.275f);
        moderatelyActive = Math.round(base * 1.35f);
        veryActive = Math.round(base * 1.525f);
        extremelyActive = Math.round(base * 1.75f);
        float[] rates = {sedentary,lightlyActive,moderatelyActive,moderatelyActive,veryActive,extremelyActive};
        return rates;
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

     //   Log.i("H", String.valueOf((double) sharedPreference.getFloat("Height",0)));
     //   Log.i("W", String.valueOf((double) sharedPreference.getFloat("Weight",0)));

        String tempFirstName = sharedPreference.getString("First Name", "");
        if(!tempFirstName.equals("")) {
           goToUserProfile();
        }

    }

    //Transfer to the user profile page
    public void goToUserProfile() {

        Intent profile = new Intent(getApplicationContext(), UserProfile.class);

        profile.putExtra("First Name", sharedPreference.getString("First Name", ""));
        profile.putExtra("Last Name", sharedPreference.getString("Last Name", ""));
        profile.putExtra("Gender", sharedPreference.getString("Gender", ""));
        profile.putExtra("Age", sharedPreference.getInt("Age", 0));
        profile.putExtra("Height", sharedPreference.getFloat("Height", 0));

        //Why are we logging weight and height here? @@@@Brandon
        Log.i("Height", BigDecimal.valueOf( sharedPreference.getFloat("Height", 0)).toPlainString());
        profile.putExtra("Weight", sharedPreference.getFloat("Weight", 0));
        Log.i("Weight", String.valueOf(sharedPreference.getFloat("Weight", 0)));
        profile.putExtra("BMI", sharedPreference.getFloat("BMI", 0));
        profile.putExtra("Sedentary",  sharedPreference.getFloat("Sedentary", 0));
        profile.putExtra("Lightly Active",  sharedPreference.getFloat("Lightly Active", 0));
        profile.putExtra("Moderately Active",  sharedPreference.getFloat("Moderately Active", 0));
        profile.putExtra("Very Active",  sharedPreference.getFloat("Very Active", 0));
        profile.putExtra("Extremely Active",  sharedPreference.getFloat("Extremely Active", 0));

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
