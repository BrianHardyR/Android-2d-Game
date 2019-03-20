package android.brian.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;

import java.io.InputStream;
import java.util.ArrayList;

public class Character {
    Canvas canvas;
    Context context;
    Movie gif;
    InputStream inputStream;
    Paint paint;
    int lives=3;
    long fps;
    int speed=170;
    int direction=0,directionY=0;
    int relTime=0;
    int centerX,centerY;
    boolean jump=false;

    int state;
    int[] resource={
            R.drawable.spr_m_traveler_idle_anim,
            R.drawable.spr_m_traveler_look_anim,
            R.drawable.spr_m_traveler_walk_anim,
            R.drawable.spr_m_traveler_run_anim,
            R.drawable.spr_m_traveler_slide_anim,
            R.drawable.spr_m_traveler_duck_anim,
            R.drawable.spr_m_traveler_jump_1up_anim,
            R.drawable.spr_m_traveler_jump_2midair_anim,
            R.drawable.spr_m_traveler_jump_3down_anim,
            R.drawable.spr_m_traveler_jump_4land_anim,
            R.drawable.spr_m_traveler_jump_complete_anim
    };
    float posX,posY;
    int width,height,wHeight,wWidth;
    long start;

    public Character(Context context, float posX, float posY, int wHeight, int wWidth){
        this.context=context;
        this.posX=posX;
        this.posY=posY;
        start=0;
        state=0;
        this.wHeight=wHeight;
        this.wWidth=wWidth;


    }

    public void setCanvas(Canvas canvas){
        this.canvas=canvas;
    }
    public void setPaint(Paint paint){this.paint=paint;}

    public void setPosition(float posX,float posY){
        this.posX=posX;
        this.posY=posY;
    }


    public void play(){
        //start gif playback
        long now = SystemClock.uptimeMillis();
        if (start==0){
            start=now;
        }
        if (gif!=null){
            int duration = gif.duration();
            if (duration == 0) {
                duration=1000;
            }
            relTime = (int) ((now - start)%duration);
            gif.setTime(relTime);
        }


    }

    public void stop(){
        //stop gif playback
        gif.setTime(0);
    }

    @SuppressLint("ResourceType")
    public void start(long fps){
        move();
        jump();
        this.fps=fps;
        inputStream = context.getResources().openRawResource(resource[state]);
        gif = Movie.decodeStream(inputStream);
        height=gif.height();
        width=gif.width();
        centerX=(int)(posX+(height/2));
        centerY=(int)(posY+(width/2));
        play();
        if (state==4) {
            gif.draw(canvas, posX, posY-20);
        }else {
            gif.draw(canvas, posX, posY);
        }

        inputStream=null;
        gif=null;

    }

    public void setState(int state){
        this.state=state;

    }
    public void setJump(boolean jump){
        this.jump=jump;
        directionY=-1;
    }

    public void move(){
        if (fps==0) {
            fps=1;
        }

        posX = posX + (direction * (speed / fps));
        if (posX<(wHeight*0.01)){
            posX= (float) (wHeight *0.01);
        }else if (posX>(wHeight*0.95)){
            posX = (float) (wHeight*0.95);
        }


    }
    public void jump(){
        if (jump){
            posY=posY+(directionY*(230/fps));
            if (posY<((wWidth/2)-100)){
                directionY=1;
            }else if (posY>(wWidth/2)){
                posY=wWidth/2;
                directionY=0;
                jump=false;
            }
        }
    }




}
