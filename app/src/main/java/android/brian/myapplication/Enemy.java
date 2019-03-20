package android.brian.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.Random;

public class Enemy extends Character{



    float cPoxX,cPosY;
    int speedX=120,speedY=120;
    boolean fallen=false;
    int directionX=0,directionY=1;
    long life;
    int maxLife;
    int action=1; //1 -fall ; 2-explode
    Random randomNumber;
    Character character;
    float rangeXmax,rangeXmin;
    float rangeYmax,rangeYmin;


    public Enemy(Context context, float posX, final float posY, final int wHeight, int wWidth,Character character) {
        super(context, posX, posY, wHeight, wWidth);
        randomNumber = new Random();
        this.resource = new int[]{
                R.drawable.spr_wasp_idle_anim,
                R.drawable.explode
        };
        this.character=character;

    }

    public void setCharPosition(float cPosX,float cPosY){
        this.cPosY=cPosY;
        this.cPoxX=cPosX;
        rangeXmax=cPoxX+60;
        rangeXmin=cPoxX-60;
        rangeYmax=cPosY+60;
        rangeYmin=cPosY-60;
    }

    public void calRemainingExpectancy(long time){
//        hit();
        life-=time;

      if (life<0 && state==0){
          setState(1);
          life = (long) (relTime);
      } else if (life<0 && state==1) {
          checkCollision();
          setState(0);
          life = (long) randomNumber.nextInt(maxLife);
          setPosition((float)(randomNumber.nextInt(wHeight)),10);

      }
    }
    public void checkCollision(){
        if (centerX>rangeXmin && centerX<rangeXmax && centerY>rangeYmin && centerY<rangeYmax){
            
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(100);
            }
            character.lives-=1;

        }

    }

    @Override
    public void move(){
        fall();
        pursue();

    }

    public void pursue(){

//        posX = (float) ((int)(posX));

        if (fps==0) {
            fps=1;
        }
        if (centerX==cPoxX||this.state==1){
            directionX=0;
        }else if (centerX<cPoxX){
            directionX=1;
        }else {
            directionX=-1;
        }

        if (centerY==cPosY||this.state==1){
            directionY=0;
        }else if (posY<cPosY){
            directionY=1;
        }else {
            directionY=-1;
        }

        posX = posX + (directionX * (speedX / fps));


    }

    public void fall(){
        if (fps==0) {
            fps=1;
        }

        posY = posY + (directionY * (speedY / fps));
        if (posY>=(wWidth/2)){
            posY=wWidth/2;
            fallen();
        }
    }

    public void fallen(){
        if (!fallen){
            fallen=true;
            life = -1;
        }

    }


}
