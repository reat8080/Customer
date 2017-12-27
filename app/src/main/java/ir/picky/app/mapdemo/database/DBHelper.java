package ir.picky.app.mapdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_FILE_NAME = "picky.db";
    public static final int DB_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DB_FILE_NAME , null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE user (fname VARCHAR, lname VARCHAR , number VARCHAR , email VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
