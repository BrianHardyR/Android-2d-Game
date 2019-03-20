package android.brian.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    SQLiteDatabase SQdatabase;
    Database database;
    TextView best,prev,current,level;
    Spinner mode;
    Button add,minus;
    int intLevel=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_fullscreen);
        Button play = findViewById(R.id.play);
        add = findViewById(R.id.add);
        minus = findViewById(R.id.minus);

//        mode = findViewById(R.id.spinner);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String selected=mode.getSelectedItem().toString();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                intent.putExtra("mode",String.valueOf(intLevel));
                startActivity(intent);
            }
        });

        SQdatabase = FullscreenActivity.this.openOrCreateDatabase("MyScore",MODE_PRIVATE,null);
        database = new Database(SQdatabase,getBaseContext());
        database.updatePreviousScore(database.getCurrentScore());
        database.updateCurrentScore(0);

        best = findViewById(R.id.txtBest);
        prev = findViewById(R.id.txtPrevious);
        current = findViewById(R.id.txtCurrent);
        level = findViewById(R.id.level);

        intLevel =Integer.parseInt(level.getText().toString());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intLevel<8) {
                    intLevel = intLevel + 1;
                    level.setText(String.valueOf(intLevel));
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intLevel>1) {
                    intLevel = intLevel - 1;
                    level.setText(String.valueOf(intLevel));
                }
            }
        });



        best.setText("Best Score: "+String.valueOf(database.getBestScore()));
        prev.setText("Previous Score: "+String.valueOf(database.getPreviousScore()));
        current.setText("Current Score: "+String.valueOf(database.getCurrentScore()));
    }

    @Override
    protected void onResume(){
        super.onResume();
        best.setText("Best Score: "+String.valueOf(database.getBestScore()));
        prev.setText("Previous Score: "+String.valueOf(database.getPreviousScore()));
        current.setText("Current Score: "+String.valueOf(database.getCurrentScore()));


    }


}

