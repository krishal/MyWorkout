package krishal.com.myworkout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.Random;

public class GenerateData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);
        final Context context = this;
        Button buttonSave = (Button) findViewById(R.id.button_generateData);
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
    }

    private void addToFile() {
        List<String[]> work = new ArrayList<>();
        String csvFile = "check.csv";
        CSVReader br = null;
        String[] exercises = {"pull up","push up","squat"};


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
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.DATE,-101);
        Random rand;
        long holder;
        for(int i = 0; i< 100;i++) {
//            holder = (i*3+20);
//            newLine[4] = holder+"";
//            holder = (i*3+40);
//            newLine[8] = holder+"";
//            holder = (i*3+100);
//            newLine[12] = holder+"";
//            holderL = myCal.getTimeInMillis()+86400000*(i-100);
//            newLine[0] = holderL+"";
            rand = new Random();
            holder = rand.nextInt(3)-1;
            myCal.add(Calendar.DATE,1);
            String[] newLine =  {myCal.getTimeInMillis()+"",exercises[0],3+"",12+"",(i*holder+20)+"",exercises[1],3+"",12+"",(i+40)+"",exercises[2],3+"",12+"",(i+100)+""};

            work.add(newLine);
        }
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
}
