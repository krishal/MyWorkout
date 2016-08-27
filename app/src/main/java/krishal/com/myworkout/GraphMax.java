package krishal.com.myworkout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlotZoomPan;
import com.androidplot.xy.XYStepMode;
import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphMax extends AppCompatActivity {

    private XYPlotZoomPan plot;
    private int numberOfInputs = 4;
    private Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_max);
        cont = this;
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

            plot = (XYPlotZoomPan) findViewById(R.id.plot);

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
            BarFormatter barFormatter = new BarFormatter();

            plot.addSeries(new SimpleXYSeries(maxNums, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Exercise"),barFormatter);


            BarRenderer renderer = (BarRenderer) plot.getRenderer( BarRenderer.class );
            renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.SIDE_BY_SIDE);
            renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.FIXED_WIDTH);

            renderer.setBarWidth(dp2px(cont,20));

            Paint series1Fill = new Paint();
            series1Fill.setColor(Color.MAGENTA);
            barFormatter.setFillPaint(series1Fill);
            plot.getLegendWidget().setVisible(false);

            // Reduce the number of range labels
            plot.setTicksPerRangeLabel(5);
            plot.setDomainBoundaries(-1, maxExer.size(), BoundaryMode.FIXED);
            plot.setDomainUpperBoundary(5,BoundaryMode.FIXED);
            plot.setRangeLowerBoundary(0,BoundaryMode.FIXED);
            plot.setRangeStep(XYStepMode.INCREMENT_BY_PIXELS,20);
            plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
            plot.setDomainLabel("Exercise");
            plot.setRangeLabel("Weight");
            plot.setTitle("Max");
            plot.setDomainValueFormat(new Format() {
                @Override
                public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
                    int day = ((Number) object).intValue();
                    if(day>-1&& day<maxExer.size())buffer.append(maxExer.get(day));
                    return buffer;
                }

                @Override
                public Object parseObject(String string, ParsePosition position) {
                    return null;
                }
            });

            plot.getGraphWidget().setDomainLabelOrientation(-45);
            plot.getGraphWidget().setDomainTickLabelVerticalOffset(dp2px(cont,30));
            plot.getGraphWidget().position(dp2px(cont,50), XLayoutStyle.ABSOLUTE_FROM_LEFT,dp2px(cont,50), YLayoutStyle.ABSOLUTE_FROM_TOP, AnchorPosition.LEFT_TOP);
            plot.getGraphWidget().setHeight(dp2px(cont,400),SizeLayoutType.ABSOLUTE);
            plot.getGraphWidget().setWidth(dp2px(cont,250),SizeLayoutType.ABSOLUTE);
            plot.getRangeLabelWidget().position(dp2px(cont,15), XLayoutStyle.ABSOLUTE_FROM_LEFT,0, YLayoutStyle.ABSOLUTE_FROM_CENTER, AnchorPosition.RIGHT_MIDDLE);
            plot.getDomainLabelWidget().position(0, XLayoutStyle.ABSOLUTE_FROM_CENTER,dp2px(cont,10), YLayoutStyle.ABSOLUTE_FROM_BOTTOM, AnchorPosition.BOTTOM_MIDDLE);
//            plot.getDomainLabelWidget().setLabelPaint(new Paint());
            plot.getRangeLabelWidget().setVisible(true);
//            plot.getRangeLabelWidget().setLabelPaint(new Paint());
            plot.getDomainLabelWidget().getLabelPaint().setTextSize(dp2px(cont,15));
            plot.getRangeLabelWidget().getLabelPaint().setTextSize(dp2px(cont, 15));
            plot.getGraphWidget().getDomainOriginLinePaint().setTextSize(dp2px(cont,10));
            plot.getGraphWidget().getRangeOriginLinePaint().setTextSize(dp2px(cont,10));
            plot.getGraphWidget().setShowDomainLabels(true);
//            plot.getGraphWidget().getDomainTickLabelPaint().setColor(Color.BLACK);
//            plot.getGraphWidget().getRangeTickLabelPaint().setColor(Color.BLACK);
            plot.getGraphWidget().setPaddingBottom(dp2px(cont,60));
            plot.getGraphWidget().setShowRangeLabels(true);
            plot.getGraphWidget().setPaddingLeft(dp2px(cont,40));
            Paint paint = new Paint();
            paint.setAlpha(0);
//            plot.getGraphWidget().setBackgroundPaint(paint);
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

    public static float dp2px(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public HashMap<String,Number> extractMaxes(LinkedList<String[]> holder){
        HashMap<String,Number> retrn = new HashMap<>();
        String[] temp;
        for(int i = 0; i < holder.size(); i++){
            temp = holder.get(i);
            for(int j = 1; j < temp.length; j= j+numberOfInputs){
                String check = (temp[j].toUpperCase().trim());
                //System.out.println(check + " " + temp[j+3]);
                if(retrn.containsKey(check) && (Integer)retrn.get(check)<Integer.parseInt(temp[j+3])){
                    //System.out.println(check+": "+retrn.get(check)+" < "+temp[j+3]);
                    retrn.put(check, Integer.parseInt(temp[j+3]));
                }
                else if(!retrn.containsKey(check)){
                    retrn.put(check, Integer.parseInt(temp[j+3]));
                }
            }
        }
        return retrn;
    }
}
