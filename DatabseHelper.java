package xami.xeeshan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xeeshan on 10/9/2017.
 */

public class DatabseHelper extends SQLiteOpenHelper
{
        public static final String DB_NAME="Students.db";
        public static final String TABLE_NAME="Students";
        private static  final String col1="Name";
    public DatabseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("create table "+ TABLE_NAME +" (NAME TEXT)");
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    ////TODO InsertFunc
    public  boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, name);
        Long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1)
            return  false;
        else
            return  true;
    }
}