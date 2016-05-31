package krishal.com.myworkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DisplayData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        printFile();
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
            List<String> w = combineArray((ArrayList)work);
            ListView lists = (ListView) findViewById(R.id.textView);
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
        return a.substring(2);
    }
}
