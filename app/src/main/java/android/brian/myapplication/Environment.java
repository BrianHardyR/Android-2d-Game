package android.brian.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Environment {
    int wHeight,wWidth;
    int[] resource={
            R.drawable.environment
    };
    Bitmap asset;
    Bitmap scaled;
    Context context;
    int fillCount;
    long scaleX,scaleY;
    float startPosX,startPosY;

    public Environment(int wHeight, int wWidth, Context context){
        this.context=context;
        this.wHeight=wHeight;
        this.wWidth=wWidth;
        scaleX=wHeight/1280*100;
        scaleY=wWidth/720*100;
        startPosX=0;
        init();
    }
    public void setStartPosY(float posY){

        this.startPosY=posY;
    }

    public void init(){
        asset=BitmapFactory.decodeResource(context.getResources(),resource[0]);
        scaled=Bitmap.createScaledBitmap(asset, asset.getWidth(), asset.getHeight(),true);
        fillCount= (int)(wHeight/asset.getWidth());
    }
    public int getFillCount(){
        return fillCount;
    }
    public float nextPosX(){
        startPosX+=asset.getWidth();
        return startPosX;
    }
    public Bitmap drawable(){
        return scaled;
    }


}
