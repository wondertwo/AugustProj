package me.wondertwo.august0802.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wondertwo on 2016/9/1.
 */
public class YuehuOpenHelper extends SQLiteOpenHelper {

    /**
     * 图片表 image_table 建表语句
     */
    private static final String CREATE_IMAGE_TABLE = "create table if not exists image_table ("
            + "id integer primary key autoincrement, "
            + "image_title text, "
            + "image_url text)";
    /**
     * 文章表 passage_table 建表语句
     */
    private static final String CREATE_PASSAGE_TABLE = "create table if not exists passage_table ("
            + "id integer primary key autoincrement, "
            + "passage_title text, "
            + "passage_url text)";

    public YuehuOpenHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_PASSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
