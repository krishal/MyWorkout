package krishal.com.myworkout;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    public void addListenerOnButton(){
        final Context context = this;

        Button butAddEx = (Button) findViewById(R.id.button_addWorkout);
        assert butAddEx != null;
        butAddEx.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, addExercise.class);
                finish();
                startActivity(intent);
            }
        });

        Button butView = (Button) findViewById(R.id.button_displayData);
        assert butView != null;
        butView.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, DisplayData.class);
                startActivity(intent);
            }
        });

        Button butViewExer = (Button) findViewById(R.id.button_display_by_exercise);
        assert butViewExer != null;
        butViewExer.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, DisplayByExercise.class);
                startActivity(intent);
            }
        });
    }
}
