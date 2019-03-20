package android.brian.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView implements Runnable{

    Thread gameThread= null;
    Thread timer;
    boolean playing;
    RendererEngine renderEngine;
    SurfaceHolder surfaceHolder;

    int wHeight,wWidth;
    int wMBL,wMUL,wMBR,wMUR;
    long fps;
    Thread maintenance;
    long thisTimeFrame=0;
    int score=0;
    int pointerCount;
    boolean startTimer=false;
    int enemies;
    Database database;

    public GameView(Context context,int wHeight,int wWidth,Database db,int enemies) {
        super(context);
        this.enemies=enemies;
        this.wHeight=wHeight;
        this.wWidth=wWidth;
        wMBL=(int) (wHeight*0.15);
        wMBR=(int) (wHeight*0.85);
        wMUL=(int) (wWidth*0.80);
        wMUR=wMUL;
        surfaceHolder = getHolder();

        renderEngine = new RendererEngine(surfaceHolder,this.getContext(),wHeight,wWidth,enemies);

        database=db;

    }

    private void setMaintenance(){
        maintenance = new Thread(new Runnable() {

            @Override
            public void run() {
                while (playing) {
                    try {
                        Thread.sleep(5000);
                        Runtime.getRuntime().gc();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        maintenance.start();
    }
    public void setTimer(){
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(startTimer && renderEngine.canDraw){
                    try {
                        Thread.sleep(2000);
                        score+=1;

                        database.updateCurrentScore(score);
                        if (database.getBestScore()<score){
                            database.updateBestScore(score);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timer.start();
    }

    @Override
    public void run() {
        while(playing){
            startTimer=true;

            long startFrameTime = System.currentTimeMillis();

            renderEngine.draw(thisTimeFrame,score);


//            invalidate();
            thisTimeFrame = System.currentTimeMillis()-startFrameTime;
            if((thisTimeFrame)>0){
                fps = 1000/thisTimeFrame;
                renderEngine.fps=fps;
            }


        }

    }

    public void pause() {
        startTimer=false;
        playing = false;
        //database.updateCurrentScore(renderEngine.score);

        try {
            timer.join();
            gameThread.join();
            maintenance.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }




    }

    // If SimpleGameEngine Activity is started then
    // start our thread.
    public void resume() {
        startTimer=true;
        playing = true;
        gameThread = new Thread(this);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread.start();
        setMaintenance();
        setTimer();

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
    if (renderEngine.canDraw) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    // Set isMoving so Bob is moved in the update method


                    if (motionEvent.getX() < (wMBL) && motionEvent.getY() > (wMUL)) {
                        renderEngine.getCharacter().direction = -1;
                        renderEngine.getCharacter().setState(2);
                    } else if (motionEvent.getX() > (wMBR) && motionEvent.getY() > (wMUR)) {
                        renderEngine.getCharacter().direction = 1;
                        renderEngine.getCharacter().setState(3);
                    } else if (motionEvent.getX() > (wMBR)) {
                        renderEngine.getCharacter().setJump(true);
                        renderEngine.getCharacter().direction = 1;
                        renderEngine.getCharacter().setState(3);
                    } else if (motionEvent.getX() < (wMBL)) {
                        renderEngine.getCharacter().setJump(true);
                        renderEngine.getCharacter().direction = -1;
                        renderEngine.getCharacter().setState(2);
                    }

                    renderEngine.touchX = motionEvent.getX();
                    renderEngine.touchY = motionEvent.getY();

                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    // Set isMoving so Bob does not move
                    renderEngine.getCharacter().direction = 0;
                    renderEngine.getCharacter().setState(0);
                    renderEngine.touchX = 0;
                    renderEngine.touchY = 0;

                    break;
            }

    }
        return true;
    }

    public RendererEngine getRenderEngine(){
        return renderEngine;
    }


}
