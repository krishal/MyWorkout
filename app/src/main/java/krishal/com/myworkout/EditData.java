package krishal.com.myworkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

public class EditData extends AppCompatActivity {

    private TextView dateView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            final String original = bundle.getString("Data");
            dateView = (TextView) findViewById(R.id.textView_date);
            String dateTemp = original.substring(0,original.indexOf(","));
            dateView.setText(dateTemp);
            TextView originalData = (TextView) findViewById(R.id.textView_data_original);
            editText = (EditText) findViewById(R.id.editText_data);
            originalData.setText(original);
            editText.setText(original.substring(original.indexOf(",")+2));

            Button butUpdate = (Button) findViewById(R.id.button_update_data);
            assert butUpdate != null;
            butUpdate.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if((countOccur(editText.getText().toString(),",")+1)%4==0) {
                        editFile();
                        finish();
                    }
                    else{
                        editText.setError("Please check formatting");
                    }
                }
            });

            ImageButton butDelete = (ImageButton) findViewById(R.id.imageButton_delete_data);
            assert butDelete != null;
            butDelete.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    deleteData();
                    finish();
                }
            });
        }

    }

    //Counts the number of occurrences for the wanted character
    private int countOccur(String line, String wanted){
        int count = line.length() - line.replace(wanted, "").length();
        return count;
    }

    private String[] splitTrim(String line, String splitAt){
        String[] retrn = line.split(splitAt);
        for(int i = 0; i<retrn.length; i++){
            retrn[i] = retrn[i].trim();
        }
        return retrn;
    }


    private void deleteData() {
        List<String[]> work = new LinkedList<>();
        String csvFile = "check.csv";
        CSVReader br=null;
        CSVWriter wr=null;
        String[] nextLine;

        try {
            FileInputStream csvStream = openFileInput(csvFile);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);

            br = new CSVReader(csvStreamReader);
            while ((nextLine = br.readNext()) != null) {
                work.add(nextLine);
            }

            String temp = dateView.getText().toString();
            int index = Integer.parseInt(temp.substring(0,temp.indexOf(".")));
            work.remove(index);

            FileOutputStream fileOutputStream = openFileOutput(csvFile, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            wr = new CSVWriter(outputStreamWriter);
            wr.writeAll(work);
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
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void editFile() {
        List<String[]> work = new LinkedList<>();
        String csvFile = "check.csv";
        CSVReader br=null;
        CSVWriter wr=null;
        String[] nextLine;

        try {
            FileInputStream csvStream = openFileInput(csvFile);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);

            br = new CSVReader(csvStreamReader);
            while ((nextLine = br.readNext()) != null) {
                work.add(nextLine);
            }

            String temp = dateView.getText().toString();
            int index = Integer.parseInt(temp.substring(0,temp.indexOf(".")));

            temp = work.get(index)[0]+","+editText.getText().toString();
            work.set(index,splitTrim(temp,","));

            FileOutputStream fileOutputStream = openFileOutput(csvFile, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            wr = new CSVWriter(outputStreamWriter);
            wr.writeAll(work);
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
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
