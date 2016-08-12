package krishal.com.myworkout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.androidplot.ui.SeriesRenderer;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYPlotZoomPan;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphMax extends AppCompatActivity {

    private XYPlot plot;
    private int numberOfInputs = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_max);
        plotFile();
    }

    private void plotFile() {
        List<String[]> work = new LinkedList<>();
        String csvFile = "check.csv";
        CSVReader br = null;
        String[] nextLine;
        HashMap<String,Number> uniqueExer = new HashMap<>();

        try {
            FileInputStream csvStream = openFileInput(csvFile);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            br = new CSVReader(csvStreamReader);
            while ((nextLine = br.readNext()) != null) {
                work.add(nextLine);
            }

            plot = (XYPlot) findViewById(R.id.plot);

//            BarFormatter barFormatter = new BarFormatter(Color.argb(200,100,100,150),Color.LTGRAY);
//            barFormatter.setPointLabelFormatter(new PointLabelFormatter());
            plot.clear();
            uniqueExer = extractMaxes((LinkedList)work);

            Iterator<Map.Entry<String,Number>> iterator = uniqueExer.entrySet().iterator();

            LinkedList<Number> maxNums = new LinkedList<>();
            final LinkedList<String> maxExer = new LinkedList<>();
            while(iterator.hasNext()){
                Map.Entry<String,Number> uniEnt = iterator.next();
                maxExer.add(uniEnt.getKey());
                maxNums.add(uniEnt.getValue());
                iterator.remove();
            }
            BarFormatter barFormatter = new BarFormatter(Color.MAGENTA,Color.LTGRAY);
            plot.addSeries(new SimpleXYSeries(maxNums, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Exercise"),barFormatter);

            // Reduce the number of range labels
            plot.setTicksPerRangeLabel(5);
            plot.setDomainBoundaries(-1, maxExer.size()-1, BoundaryMode.FIXED);
            plot.setRangeLowerBoundary(0,BoundaryMode.FIXED);
            plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS,20);
            plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
            plot.setDomainLabel("Exercise");
            plot.setRangeLabel("Weight");
            plot.setTitle("Max");
            plot.setDomainValueFormat(new Format() {
                @Override
                public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
                    int day = ((Number) object).intValue() % (maxExer.size());
                    if(day>-1&& day<maxExer.size())buffer.append(maxExer.get(day));
                    return buffer;
                }

                @Override
                public Object parseObject(String string, ParsePosition position) {
                    return null;
                }
            });

            plot.getBackgroundPaint().setAlpha(0);
            plot.getGraphWidget().getBackgroundPaint().setAlpha(0);
            plot.getGraphWidget().getGridBackgroundPaint().setAlpha(0);

            plot.redraw();

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

    public HashMap<String,Number> extractMaxes(LinkedList<String[]> holder){
        HashMap<String,Number> retrn = new HashMap<>();
        String[] temp;
        for(int i = 0; i < holder.size(); i++){
            temp = holder.get(i);
            for(int j = 1; j < temp.length; j= j+numberOfInputs){
                if(retrn.containsKey(temp[j]) && (Integer)retrn.get(temp[j])<Integer.parseInt(temp[j+3])){
                    retrn.put(temp[j], Integer.parseInt(temp[j+3]));
                }
                else {
                    retrn.put(temp[j], Integer.parseInt(temp[j+3]));
                }
            }
        }
        return retrn;
    }
}
