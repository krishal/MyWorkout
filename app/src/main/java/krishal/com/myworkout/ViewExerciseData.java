package krishal.com.myworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewExerciseData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise_data);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String exercise = bundle.getString(getString(R.string.intentExer));
        }
    }
}
