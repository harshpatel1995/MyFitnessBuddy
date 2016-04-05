package squad.myfitnessbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class LogSets extends AppCompatActivity {

    ListView addSetsLV;
    private Button addSetBT;
    EditText repET, weightET;
    private ArrayList<String> strArr;
    private ArrayAdapter<String> adapter;

    public void onClickAddSet(View view){
        repET = (EditText) findViewById(R.id.repET);
        weightET = (EditText) findViewById(R.id.weightET);
        //EditText[] editTextArray = {repET, weightET};

        String[] errorMessages = {
                "Reps cannot be empty!",
                "Weight cannot be empty!"
        };

        int reps, weight;

        reps = Integer.parseInt(repET.getText().toString());
        weight = Integer.parseInt(weightET.getText().toString());

        if(repET.getText().toString().matches("")){
            Toast.makeText(this, errorMessages[0],Toast.LENGTH_SHORT).show();

        }
        else if(weightET.getText().toString().matches("")){
            Toast.makeText(this, errorMessages[1], Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i("Add Set: ", "Reps- "+Integer.toString(reps)+" Weight-"+Integer.toString(weight));

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_sets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Log Workout");

        addSetsLV = (ListView) findViewById(R.id.addedSetsLV);
        repET = (EditText) findViewById(R.id.repET);
        weightET = (EditText) findViewById(R.id.weightET);

        strArr = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strArr);
        addSetsLV.setAdapter(adapter);

        addSetBT = (Button) findViewById(R.id.addSetBT);
        String[] errorMessages = {
                "Reps cannot be empty!",
                "Weight cannot be empty!"
        };

        // int reps, weight;




        addSetBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int reps = Integer.parseInt(repET.getText().toString());
                int weight = Integer.parseInt(weightET.getText().toString());

                strArr.add("Reps: "+Integer.toString(reps)+" Weights: "+Integer.toString(weight));
                adapter.notifyDataSetChanged();

                repET.setText("");
                weightET.setText("");
            }
        });








    }



}
