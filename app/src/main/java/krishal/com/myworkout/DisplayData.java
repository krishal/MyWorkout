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
import com.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        printFile();
        addListenerOnButton();
        final ListView listView = (ListView) findViewById(R.id.list_all_data);
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                final String item = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(context, EditData.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Data", position+". "+item);
                startActivity(intent);
            }
        });
    }

    public void addListenerOnButton() {
        Button buttonClear = (Button) findViewById(R.id.button_clear_data);
        assert buttonClear != null;
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearData();
            }
        });
    }

    private void clearData() {
        List<String[]> work = new ArrayList<>();
        String csvFile = "check.csv";
        CSVWriter wr = null;

        try {
            FileOutputStream fileOutputStream = openFileOutput(csvFile, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            wr = new CSVWriter(outputStreamWriter);
            wr.writeAll(work);

            List<String> w = new ArrayList<>();
            ListView lists = (ListView) findViewById(R.id.list_all_data);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, w);
            assert lists != null;
            lists.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            List<String> w = combineArray((ArrayList)work, ", ");
            ListView lists = (ListView) findViewById(R.id.list_all_data);

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

    public ArrayList combineArray(ArrayList<String[]> t, String separator) {
        List<String> temp = new ArrayList<>(t.size());
        for (int i = 0; i < t.size(); i++) {
            temp.add(i, join(separator, t.get(i)));
        }
        return (ArrayList) temp;
    }

    public String join(String sep, String[] the){
        long converter = Long.parseLong(the[0]);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(converter);
        String a = cal.get(Calendar.YEAR)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DATE)+"";
        for (int i = 1; i<=the.length-1;i++) {
            a = a + sep + the[i];
        }
        return a;
    }
}
