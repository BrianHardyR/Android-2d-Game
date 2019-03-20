package android.brian.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Database {

    SQLiteDatabase DB;
    Cursor cursor;
    Context context;

    public Database(SQLiteDatabase db,Context context){
        DB=db;
        this.context=context;
        setupDb();
    }

    public void setupDb(){
        DB.execSQL("CREATE TABLE IF NOT EXISTS MyTable " +
                "(id int,name varchar, bestscore int, previousscore int,currentscore int);");
        //insert records into table
        if (getNumRec()<1) {
            DB.execSQL("INSERT INTO MyTable VALUES(1,'Player',0,0,0)");
            Toast.makeText(context,"record created",Toast.LENGTH_SHORT);
        }

    }

    public int getNumRec(){
        cursor = DB.query("MyTable", null,null,null,null,null,null);
        int count =0;
        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
          count++;
        }
        return count;
    }
    public void updateName(String name){

        String query="UPDATE MyTable SET name='"+name+"' WHERE id=1";
        DB.execSQL(query);

    }
    public void updateBestScore(int score){

        String query="UPDATE MyTable SET bestscore='"+score+"' WHERE id=1";
        DB.execSQL(query);

    }
    public void updatePreviousScore(int score){

        String query="UPDATE MyTable SET previousscore='"+score+"' WHERE id=1";
        DB.execSQL(query);

    }
    public void updateCurrentScore(int score){

        String query="UPDATE MyTable SET currentscore="+score+" WHERE id=1";
        DB.execSQL(query);

    }
    public String getName(){
        cursor = DB.query("MyTable", null,null,null,null,null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        return  name;
    }
    public int getBestScore(){
        cursor = DB.query("MyTable", null,null,null,null,null,null);
        cursor.moveToFirst();
        int score = cursor.getInt(cursor.getColumnIndex("bestscore"));
        return  score;
    }
    public int getPreviousScore(){
        cursor = DB.query("MyTable", null,null,null,null,null,null);
        cursor.moveToFirst();
        int score = cursor.getInt(cursor.getColumnIndex("previousscore"));
        return  score;
    }
    public int getCurrentScore(){
        cursor = DB.query("MyTable", null,null,null,null,null,null);
        cursor.moveToLast();
        int score = cursor.getInt(cursor.getColumnIndex("currentscore"));
        return  score;
    }

}
