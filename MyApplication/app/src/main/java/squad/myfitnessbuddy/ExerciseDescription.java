package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.drawable.AnimationDrawable;

public class ExerciseDescription extends MenuButtonBar {
    SharedPreferences sharedPreference;
    TextView exerciseNameTV,descriptionTV;
    ImageView anim;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Exercise Description");

        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        exerciseNameTV =(TextView) findViewById(R.id.exerciseNameTV);
        exerciseNameTV.setText(sharedPreference.getString(ConstantValues.cSP_CURRENT_EXERCISE_TO_LOG, ""));

       checkForDescription(exerciseNameTV.getText().toString().trim());
    }

    public void onBackButtonClicked (View view){
        finish();
    }

    protected void checkForDescription(String exercise) {
        descriptionTV = (TextView)findViewById(R.id.descriptions);
        anim = (ImageView)findViewById(R.id.workoutGraphic);

        descriptionTV.setMovementMethod(new ScrollingMovementMethod());

        switch(exercise) {
            case"Arnold Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.arnold));
                descriptionTV.setText(R.string.arnold);
                break;
            case "Barbell Back Squat":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_back_squat));
                descriptionTV.setText(R.string.barbell_back_squat);
                break;
            case "Barbell Bench Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_bench_press));
                descriptionTV.setText(R.string.barbell_bench_press);
                break;
            case "Barbell Biceps Curl":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_bicep_curl));
                descriptionTV.setText(R.string.barbell_biceps_curl);
                break;
            case "Barbell Deadlift":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_deadlift));
                descriptionTV.setText(R.string.barbell_deadlift);
                break;
            case "Barbell Front Squat":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_front_squat));
                descriptionTV.setText(R.string.barbell_front_squat);
                break;
            case "Barbell Stiff Legged Deadlift":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.barbell_stiff_legged_deadlift));
                descriptionTV.setText(R.string.barbell_stiff_legged_deadlift);
                break;
            case "Close Grip Barbell Bench Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.close_grip_barbell_bench_press));
                descriptionTV.setText(R.string.close_grip_barbell_bench_press);
                break;
            case "Concentration Curl":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.concentration_curl));
                descriptionTV.setText(R.string.concentration_curl);
                break;
            case "Donkey Calf Raise":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.donkey_calf_raise));
                descriptionTV.setText(R.string.donkey_calf_raise);
                break;
            case "Dumbbell Front Raise":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dumbbell_front_raise));
                descriptionTV.setText(R.string.dumbbell_front_raise);
                break;
            case "Dumbbell Lateral Raise":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dumbbell_lateral_raise));
                descriptionTV.setText(R.string.dumbbell_lateral_raise);
                break;
            case "Dumbbell Shoulder Shrugs":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dumbbell_shoulder_shrugs));
                descriptionTV.setText(R.string.dumbbell_shoulder_shrugs);
                break;
            case "Dumbbell Fly":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dumbbell_fly));
                descriptionTV.setText(R.string.dumbbell_fly);
                break;
            case "Hammer Curl":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hammer_curl));
                descriptionTV.setText(R.string.hammer_curl);
                break;
            case "Inclined Barbell Bench Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.incline_barbell_bench_press));
                descriptionTV.setText(R.string.incline_barbell_bench_press);
                break;
            case "Lat Pulldown":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.lat_pulldown));
                descriptionTV.setText(R.string.lat_pulldown);
                break;
            case "Leg Curl":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.leg_curl));
                descriptionTV.setText(R.string.leg_curl);
                break;
            case "Leg Extension":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.leg_extension));
                descriptionTV.setText(R.string.leg_extension);
                break;
            case "Preacher Curl":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.preacher_curl));
                descriptionTV.setText(R.string.precher_curl);
                break;
            case "Reverse Dumbbell Fly":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.reverse_dumbbell_fly));
                descriptionTV.setText(R.string.reverse_dumbbell_fly);
                break;
            case "Rope Triceps Extension":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rope_triceps_extension));
                descriptionTV.setText(R.string.rope_triceps_extension);
                break;
            case "Seated Cable Row":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.seated_cable_row));
                descriptionTV.setText(R.string.seated_cable_row);
                break;
            case "Seated Calf Raise":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.seated_calf_raise));
                descriptionTV.setText(R.string.seated_calf_raise);
                break;
            case "Seated Military Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.seated_military_press));
                descriptionTV.setText(R.string.seated_military_press);
                break;
            case "Standing French Press":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.standing_french_press));
                descriptionTV.setText(R.string.standing_french_press);
                break;
            case "Standing T-Bar Row":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.standing_t_bar_row));
                descriptionTV.setText(R.string.standing_t_bar_row);
                break;
            case "Weighted Glute Bridge":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weighted_glute_bridge));
                descriptionTV.setText(R.string.weighted_glute_bridge);
                break;
            case "Weighted Pullups":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weighted_pullup));
                descriptionTV.setText(R.string.weighted_pullup);
                break;
            case "Weighted Triceps Dip":
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.weighted_tricep_dip));
                descriptionTV.setText(R.string.weighted_tricep_dip);
                break;
            default:
                anim.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dumbbell_lateral_raise));
                descriptionTV.setText(R.string.test);
                break;

        }
        anim.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                final AnimationDrawable animationDrawable = (AnimationDrawable) anim.getDrawable();
                animationDrawable.start();
            }
            @Override
            public void onViewDetachedFromWindow(View view) {
            }
        });

    }
}



