package krishal.com.myworkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static java.util.Arrays.asList;

public class DisplayByExercise extends AppCompatActivity {

    int numberOfInputs = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_by_exercise);
        printFile();
        final ListView listView = (ListView) findViewById(R.id.list_exercises_data);
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                final String item = (String) parent.getItemAtPosition(position);
                Intent  intent = new Intent(context, DisplayData.class);

                startActivity(intent);
            }
            }
        );
    }

    private void printFile() {
        List<String[]> work = new ArrayList<>();
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
            List<String> w = extractUniqueExer((ArrayList)work);
            ListView lists = (ListView) findViewById(R.id.list_exercises_data);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, w);

            assert lists != null;
            lists.setAdapter(adapter);

            adapter.notifyDataSetChanged();
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

    public List<String> extractUniqueExer(ArrayList<String[]> holder){
        LinkedHashSet check = new LinkedHashSet();
        String[] temp;
        for(int i = 0; i < holder.size(); i++){
            temp = holder.get(i);
            for(int j = 1; j < temp.length; j= j+numberOfInputs){
                check.add(temp[j]);
            }
        }
        String[] k = new String[check.size()];
        Iterator us = check.iterator();
        int i = 0;
        while(us.hasNext()){
            k[i] = us.next().toString();
            i++;
        }
        return asList(k);
    }
}
