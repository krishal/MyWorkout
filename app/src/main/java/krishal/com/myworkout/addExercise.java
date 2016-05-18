package krishal.com.myworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.lang.String;

public class addExercise extends AppCompatActivity {

    List<String[]> workouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

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

    public void addToFile() {

        String csvFile = "src/main/java/log.csv";
        CSVReader br = null;
        String[] nextLine;
        String[] newLine = {"1", "2"};
        char csvSplitBy = ',';

        try {
            br = new CSVReader(new FileReader(csvFile), csvSplitBy);
            while ((nextLine = br.readNext()) != null) {
                workouts.add(nextLine);
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
        printList(workouts);
        try {
            wr = new CSVWriter(new FileWriter(csvFile), csvSplitBy);
            workouts.add(newLine);
            wr.writeAll(workouts);
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
        return a;
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
