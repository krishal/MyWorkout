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

        Button button = (Button) findViewById(R.id.button_addWorkout);
        assert button != null;
        button.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, addExercise.class);
                startActivity(intent);
            }
        });
    }
}
