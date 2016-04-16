package squad.myfitnessbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.TreeSet;

public class Graph extends MenuButtonBar {

    SharedPreferences sharedPreference;
    TextView exerciseNameTV;
    //go back a page
    public void onBackButtonClicked (View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Max Rep Statistics");

        sharedPreference = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        exerciseNameTV = (TextView) findViewById(R.id.exerciseNameTV);
        exerciseNameTV.setText(sharedPreference.getString(ConstantValues.cSP_CURRENT_EXERCISE_TO_LOG, ""));

        LineChart chart = (LineChart) findViewById(R.id.chart);

        CustomMarkerView marker = new CustomMarkerView(this, R.layout.graph_marker);
        chart.setMarkerView(marker);
        chart.setExtraLeftOffset(5);
        chart.setExtraRightOffset(5);
        chart.setExtraBottomOffset(5);

        XAxis xAxis = chart.getXAxis();
        YAxis yAxisLeft = chart.getAxisLeft();
        YAxis yAxisRight = chart.getAxisRight();
        Legend legend = chart.getLegend();

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(180f, 0));
        entries.add(new Entry(225f, 1));
        entries.add(new Entry(135f, 2));
        entries.add(new Entry(185f, 3));
        entries.add(new Entry(200f, 4));
        entries.add(new Entry(135f, 5));

        LineDataSet line = new LineDataSet(entries, "Max Rep");

        ArrayList<String> horizontalLabel = new ArrayList<String>();
        horizontalLabel.add("Jan");
        horizontalLabel.add("Feb");
        horizontalLabel.add("Mar");
        horizontalLabel.add("Apr");
        horizontalLabel.add("May");
        horizontalLabel.add("Jun");

        LineData data = new LineData(horizontalLabel, line);
        chart.setData(data); // set the data and list of lables into chart

        chart.setDescription("Max Rep Per Workout");  // set the description
        chart.setDrawBorders(true);
        chart.setBorderColor(ContextCompat.getColor(this, R.color.colorPrimary));

        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14);
        xAxis.setLabelRotationAngle(-70);
        xAxis.setLabelsToSkip(0);
        xAxis.setAxisLineColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        yAxisLeft.setTextSize(14);
        yAxisLeft.setAxisLineWidth(1);
        yAxisLeft.setAxisLineColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        yAxisRight.setEnabled(false);

        legend.setEnabled(false);

        line.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        line.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        line.setCircleRadius(4);
        line.setLineWidth(2);
        line.setDrawValues(false);
    }

    @Override
    protected void onRestart() {

        SQLiteDatabase exerciseDB;

        try {
            exerciseDB = this.openOrCreateDatabase("mfbDatabase.db", MODE_PRIVATE, null);
            exerciseDB.execSQL(ConstantValues.cCREATE_OR_OPEN_WORKOUT_LOGS_DATABASE_SQL);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onRestart();
    }

    public class CustomMarkerView extends MarkerView {

        private TextView maxRepTV;

        public CustomMarkerView (Context context, int layoutResource) {
            super(context, layoutResource);
            // this markerview only displays a textview
            maxRepTV = (TextView) findViewById(R.id.maxRepTV);
        }

        // callbacks everytime the MarkerView is redrawn
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            maxRepTV.setText("" + e.getVal()); // set the entry-value as the display text
        }

        @Override
        public int getXOffset(float xpos) {
            // this will center the marker-view horizontally
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset(float ypos) {
            // this will center the marker-view vertically
            return -(getHeight() / 2);
        }
    }
}
