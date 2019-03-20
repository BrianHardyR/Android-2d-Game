package android.brian.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

public class RendererEngine extends Activity{

    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Context context;
    Character character;
    Random randomNum;
    ArrayList<Enemy> enemies;
    Environment environment;
    Bitmap up,down,left,right;
    int noOfEnemies=5;
    int maxLife;
    Paint paint;
    long fps;
    boolean canDraw=true;
    float touchX,touchY;

    int wHeight,wWidth;
    int wMBL,wMUL,wMBR,wMUR;
    int score,time;

    public RendererEngine(SurfaceHolder surfaceHolder, Context context, int wHeight, int wWidth,int noOfEnemies){
        this.wHeight=wHeight;
        this.wWidth=wWidth;
        wMBL=(int) (wHeight*0.15);
        wMBR=(int) (wHeight*0.85);
        wMUL=(int) (wWidth*0.80);
        wMUR=wMUL;
        this.noOfEnemies=noOfEnemies;
        this.surfaceHolder=surfaceHolder;
        this.context=context;
        randomNum = new Random();
        environment=new Environment(wHeight,wWidth,context);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(45);
        character = new Character(context,wHeight/2,wWidth/2,wHeight,wWidth);
        character.setPaint(paint);

//        environment.setStartPosY((wWidth/2)+character.height);
        environment.setStartPosY(0);
        enemies = new ArrayList<>();
        createEnemies();
        environment.setStartPosY(-20);
        up = BitmapFactory.decodeResource(context.getResources(),R.drawable.up);
        down = BitmapFactory.decodeResource(context.getResources(),R.drawable.down);
        left = BitmapFactory.decodeResource(context.getResources(),R.drawable.left);
        right = BitmapFactory.decodeResource(context.getResources(),R.drawable.right);

    }

    public void draw(long time,int score){

            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.DKGRAY);

                for (environment.startPosX = 0; environment.startPosX < 1280; environment.nextPosX()) {
                    canvas.drawBitmap(environment.drawable(), environment.startPosX, environment.startPosY, paint);

                }

                canvas.drawText("FPS:" + fps, 20, 40, paint);
                canvas.drawText("x:" + touchX + " y:" + touchY, 20, 80, paint);
                canvas.drawText(wHeight + " :" + wWidth, 20, 120, paint);
                canvas.drawText("Scale:"+environment.scaleX+":"+environment.scaleY,20,150,paint);
                canvas.drawText("Score :" + score, wMBR-120, 40, paint);
                canvas.drawText("Lives:" + character.lives, wMBR-120, 80, paint);
                character.setCanvas(canvas);
                character.start(fps);
                if (character.lives <= 0) {
                    character.setState(9);
                    character.direction=0;
                    canvas.drawText("Game Over", (wHeight / 2) - 100, wWidth / 2, paint);
                    canDraw=false;
                } else {

                    if (enemies != null) {
                        for (Enemy enemy : enemies) {
                            enemy.setCharPosition(character.centerX, character.centerY);
                            enemy.calRemainingExpectancy(time);
                            enemy.setCanvas(canvas);
                            enemy.start(fps);
                        }
                    }
                }
                canvas.drawBitmap(right,wMBR,wMUR,paint);
                canvas.drawBitmap(left,wMBL-140,wMUL,paint);
                canvas.drawBitmap(up,wMBR,wMUR-130,paint);
                canvas.drawBitmap(up,wMBL-140,wMUL-130,paint);

                surfaceHolder.unlockCanvasAndPost(canvas);
                canvas = null;

            }


    }

    public void createEnemies(){
        maxLife=11000;
        for (int count = 0;count<noOfEnemies && (enemies.size()<noOfEnemies || enemies==null);count++){
            Enemy e = new Enemy(context,(float)(randomNum.nextInt(wHeight)),10,wHeight,wWidth,character);
            e.life= (long) randomNum.nextInt(maxLife);
            e.setPaint(paint);
            e.maxLife=maxLife;
            enemies.add(e);

        }
    }

    public Character getCharacter(){
        return character;
    }
    public Canvas getCanvas(){
        return canvas;
    }
    public void setEnemyNo(int x){
        this.noOfEnemies=x;
    }



}
