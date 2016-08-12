package krishal.com.myworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class ViewExerciseData extends AppCompatActivity {

    private int numberOfInputs = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_data);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String exercise = bundle.getString(getString(R.string.intentExer));
            printFile(exercise);
        }
    }

    private void printFile(String exercise) {
        List<String[]> work = new LinkedList<>();
        String csvFile = "check.csv";
        CSVReader br = null;
        String[] nextLine;

        try {
            FileInputStream csvStream = openFileInput(csvFile);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            br = new CSVReader(csvStreamReader);
            while ((nextLine = br.readNext()) != null) {
                work.add(nextLine);
            }

            GraphView graph = (GraphView) findViewById(R.id.graph);

            LineGraphSeries<DataPoint> w = extractUniqueExer((LinkedList)work, exercise);
            graph.addSeries(w);

            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ViewExerciseData.this));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

            Calendar cal = Calendar.getInstance();
            Date d1 = cal.getTime();
            cal.add(Calendar.MONTH, -1);
            Date d2 = cal.getTime();
            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(d2.getTime());
            graph.getViewport().setMaxX(d1.getTime());
            graph.getViewport().setXAxisBoundsManual(true);

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not nessecary

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public LineGraphSeries<DataPoint> extractUniqueExer(LinkedList<String[]> holder, String exer){
        ArrayList<DataPoint> lineGraph = new ArrayList<>();
        String[] temp;
        Calendar calendar = Calendar.getInstance();
        Date date;
        for(int i = 0; i < holder.size(); i++){
            temp = holder.get(i);
            for(int j = 1; j < temp.length; j= j+numberOfInputs){
                if(temp[j].compareToIgnoreCase(exer)==0){
                    calendar.setTimeInMillis(Long.parseLong(temp[0]));
                    date = calendar.getTime();
                    lineGraph.add(new DataPoint(date, Integer.parseInt(temp[j+3])));
                }
            }
        }
        DataPoint[] k = new DataPoint[lineGraph.size()];
        Iterator us = lineGraph.iterator();
        int i = 0;
        while(us.hasNext()){
            k[i] = (DataPoint) us.next();
            i++;
        }
        return new LineGraphSeries(k);
    }
}
