package android.brian.myapplication;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {


    GameView gameView;
    SQLiteDatabase SQdatabase;
    Database database;
    int enemies=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        enemies=Integer.parseInt(getIntent().getStringExtra("mode"));
        SQdatabase = MainActivity.this.openOrCreateDatabase("MyScore",MODE_PRIVATE,null);
        database = new Database(SQdatabase,getBaseContext());
        gameView = new GameView(this,displayMetrics.widthPixels,displayMetrics.heightPixels,database,enemies);
        setContentView(gameView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
//        Toast.makeText(getBaseContext(),String.valueOf(gameView.getRenderEngine().score),Toast.LENGTH_LONG).show();
        gameView.pause();

    }

}
