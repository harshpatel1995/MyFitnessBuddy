package squad.myfitnessbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class CreateWorkout extends AppCompatActivity {

    public ListView exerciseLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Customize the Actionbar color to 'Black' and text to 'Setup Page'
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000"))); // set your desired color
        actionBar.setTitle("Create Workout");

        Intent intent = getIntent();

        exerciseLV = (ListView) findViewById(R.id.exercisesLV);

        populateExercises();


    }


    protected void populateExercises(){

     /*   ListView lv = (ListView) findViewById(R.id.myListView);
        final ArrayList<String> contacts = new ArrayList<>();

        contacts.add("Harsh Patel");
        contacts.add("COP 4331");
        contacts.add("MyFitness Buddy");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 					android.R.layout.simple_list_item_1, contacts);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(contacts.get(position) + " : " + id);
                Toast.makeText(getApplicationContext(), "Hey " + contacts.get(position), Toast.LENGTH_LONG).show();
            }
        });

        */

        final ArrayList<String> exercisesList = new ArrayList<>();

        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        try {
            SQLiteDatabase exerciseDB = myDbHelper.getReadableDatabase();

            Cursor c = exerciseDB.rawQuery("SELECT * FROM exercises ORDER BY name", null);

            int nameIndex = c.getColumnIndex("name");

            c.moveToFirst();

            while (c != null) {

               exercisesList.add(c.getString(nameIndex));
                c.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exercisesList);
        exerciseLV.setAdapter(adapter);

        exerciseLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(exercisesList.get(position) + " : " + id);
                Toast.makeText(getApplicationContext(), "Hey " + exercisesList.get(position), Toast.LENGTH_LONG).show();
            }

        });
    }


}


