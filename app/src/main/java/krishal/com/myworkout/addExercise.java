package krishal.com.myworkout;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.lang.String;

public class addExercise extends AppCompatActivity {

    List<String[]> workouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        datePickDia();
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        Button buttonD = (Button) findViewById(R.id.button_date);
        assert buttonD != null;
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDia();
            }
        });
        Button button = (Button) findViewById(R.id.button_saveWorkout);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToFile();
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

        String e = exer.getText().toString().trim();
        String s = set.getText().toString().trim();
        String r = rep.getText().toString().trim();

        if (e.equals("") || s.equals("") || r.equals("")) {
            if (e.equals("")) {
                exer.setError("Field cannot be left blank");
            }
            if (s.equals("")) {
                set.setError("Field cannot be left blank");
            }
            if (r.equals("")) {
                rep.setError("Field cannot be left blank");
            }
        } else {
            String[] what = {e, s, r};
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
        }
    }

    private void datePickDia() {
        Calendar myCal = Calendar.getInstance();
        new DatePickerDialog(this, datePickerListener, myCal.YEAR, myCal.MONTH, myCal.DATE);
    }

    int year, month, day;
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String tem = day+"-"+month+"-"+year;
            Button buttonD = (Button) findViewById(R.id.button_date);
            buttonD.setText(tem);
        }
    };

    private void addToFile() {
        List<String[]> work = new ArrayList<>();
        String csvFile = "src/main/java/log.csv";
        CSVReader br = null;
        String[] nextLine;
        String[] newLine;
        char csvSplitBy = ',';

        try {
            br = new CSVReader(new FileReader(csvFile), csvSplitBy);
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
        newLine = listIntoOneLine((ArrayList)workouts);
        newLine[0] = "\""+year+month+day+"\"";
        try {
            wr = new CSVWriter(new FileWriter(csvFile), csvSplitBy);
            work.add(newLine);
            wr.writeAll(work);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(new File(".").getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String[] listIntoOneLine(ArrayList<String[]> h){
        String[] ret = new String[h.size()*3+1];
        String[] temp;
        for(int i = 0; i < h.size(); i++){
            temp = h.get(i);
            for(int j = 0; j < temp.length; j++){
                ret[i*3+j+1] = temp[j];
            }
        }
        return ret;
    }

    public ArrayList combineArray(ArrayList<String[]> t) {
        List<String> temp = new ArrayList<>(t.size());
        for (int i = 0; i < t.size(); i++) {
            temp.add(i, join(", ", t.get(i)));
        }
        return (ArrayList) temp;
    }

    public String join(String sep, String[] the){
        String a = "";
        for(int j = 0; j < the.length; j++){
            a = a + sep + the[j];
        }
        return a.substring(2);
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
