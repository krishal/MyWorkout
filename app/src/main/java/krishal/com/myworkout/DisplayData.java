package krishal.com.myworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            printList((ArrayList)work);
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

    public String printList(ArrayList<String[]> h){
        String ret = "";
        String[] temp;
        for(int i = 0; i < h.size(); i++){
            temp = h.get(i);
            for(int j = 0; j < temp.length; j++){
                ret = ret + ", " + temp[j];
            }
            ret = ret + "/n";
        }
        return ret;
    }
}
