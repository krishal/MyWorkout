package krishal.com.myworkout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.lang.String;

public class addExercise extends AppCompatActivity {

    List<String[]> workouts = new ArrayList<>();
    int numberOfEntries = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        EditText exer = (EditText) findViewById(R.id.edittext_exercise);
        exer.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        datePickDia();
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        Button buttonSave = (Button) findViewById(R.id.button_saveWorkout);
        assert buttonSave != null;
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToFile();
                Intent intent = new Intent(context, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        Button buttonAddTL = (Button) findViewById(R.id.button_addToList);
        assert buttonAddTL != null;
        buttonAddTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList();
            }
        });
    }

    public void addToList() {
        EditText exer = (EditText) findViewById(R.id.edittext_exercise);
        EditText set = (EditText) findViewById(R.id.edittext_sets);
        EditText rep = (EditText) findViewById(R.id.edittext_reps);
        EditText wei = (EditText) findViewById(R.id.edittext_weight);

        String e = exer.getText().toString().trim();
        String s = set.getText().toString().trim();
        String r = rep.getText().toString().trim();
        String w = wei.getText().toString().trim();

        if (e.equals("") || s.equals("") || r.equals("")||w.equals("")) {
            if (e.equals("")) {
                exer.setError("Field cannot be left blank");
            }
            if (s.equals("")) {
                set.setError("Field cannot be left blank");
            }
            if (r.equals("")) {
                rep.setError("Field cannot be left blank");
            }
            if (w.equals("")) {
                wei.setError("Field cannot be left blank");
            }
        } else {
            String[] what = {e, s, r, w};
            workouts.add(what);

            List<String> work = combineArray((ArrayList) workouts);
            ListView lists = (ListView) findViewById(R.id.list_exercises);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, work);
            assert lists != null;
            lists.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            exer.setError(null);
            set.setError(null);
            rep.setError(null);
            wei.setError(null);
        }
    }

    private void datePickDia() {
        Calendar myCal = Calendar.getInstance();
        EditText day = (EditText) findViewById(R.id.editText_day);
        EditText month = (EditText) findViewById(R.id.editText_month);
        EditText year = (EditText) findViewById(R.id.editText_year);

        year.setText(myCal.get(myCal.YEAR)+"");
        month.setText(myCal.get(myCal.MONTH)+"");
        day.setText(myCal.get(myCal.DATE)+"");
    }

    private void addToFile() {
        List<String[]> work = new ArrayList<>();
        String csvFile = "check.csv";
        CSVReader br = null;
        String[] newLine;

        try {
            FileInputStream csvStream = openFileInput(csvFile);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            br = new CSVReader(csvStreamReader);
            String[] nextLine;
            while ((nextLine = br.readNext()) != null) {
                work.add(nextLine);
            }
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
        CSVWriter wr = null;
        printList(work);

        EditText day = (EditText) findViewById(R.id.editText_day);
        EditText month = (EditText) findViewById(R.id.editText_month);
        EditText year = (EditText) findViewById(R.id.editText_year);
        newLine = listIntoOneLine((ArrayList)workouts);

        int ye = Integer.parseInt(year.getText()+"");
        int mon = Integer.parseInt(month.getText()+"");
        int da = Integer.parseInt(day.getText()+"");

        Calendar myCal = Calendar.getInstance();
        myCal.set(ye,mon,da);

        newLine[0] = ""+myCal.getTimeInMillis();

/*        if(work.size()==0){*/
            work.add(newLine);
/*        } else if(work.get(0)[0].compareTo(newLine[0])>=0){
            work.add(0,newLine);
        } else if(work.get(work.size()-1)[0].compareTo(newLine[0])<=0){
            work.add(newLine);
        } else{
            int insertIndx = insertIndex((ArrayList)work, 0, work.size(), newLine[0]);
            work.add(insertIndx,newLine);
        }*/

        try {
            FileOutputStream fileOutputStream = openFileOutput(csvFile, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            wr = new CSVWriter(outputStreamWriter);
            wr.writeAll(work);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(new File(".").getAbsoluteFile());
        } finally {
            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*    private int insertIndex(ArrayList work, int start, int end, String s) {
        int mid = (end-start)/2+start;
        if(start>end){
            return 0;
        }else if(work.get(mid))
    }*/

    public String[] listIntoOneLine(ArrayList<String[]> h){
        String[] ret = new String[h.size()*numberOfEntries+1];
        String[] temp;
        for(int i = 0; i < h.size(); i++){
            temp = h.get(i);
            for(int j = 0; j < temp.length; j++){
                ret[i*numberOfEntries+j+1] = temp[j];
            }
        }
        return ret;
    }

    public ArrayList combineArray(ArrayList<String[]> t) {
        List<String> temp = new ArrayList<>(t.size());
        for (int i = 0; i < t.size(); i++) {
            temp.add(i, join(",", t.get(i)));
        }
        return (ArrayList) temp;
    }

    public String join(String sep, String[] the){
        String a = "";
        for(int j = 0; j < the.length; j++){
            a = a + sep + the[j];
        }
        return a.substring(1);
    }

    public void printList(List<String[]> t) {
        for (String[] next : t) {
            for (String n : next) {
                System.out.print(n + ", ");
            }
            System.out.println();
        }
    }
}
