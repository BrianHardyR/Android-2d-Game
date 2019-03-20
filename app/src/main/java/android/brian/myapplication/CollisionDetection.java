package android.brian.myapplication;

public class CollisionDetection implements Runnable{

    int cPosX,cPosY,ePosX,ePosY,cState,eState;
    boolean run=false;
    int time=0;
    GameView game;
    public CollisionDetection(GameView game){
        this.game=game;
    }

    public void setState(int cPosX,int cPosY,int ePosX,int ePosY,int cState,int eState){

    }

    @Override
    public void run() {

        while (run){
            try {
                Thread.sleep(2000);
                time+=2;
                game.score=time;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
