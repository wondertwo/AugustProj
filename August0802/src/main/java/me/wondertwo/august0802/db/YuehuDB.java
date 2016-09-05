package me.wondertwo.august0802.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wondertwo on 2016/9/2.
 */
public class YuehuDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "yuehu_db";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static YuehuDB dbInstance; //

    private SQLiteDatabase sqliteDB;

    /**
     * 将构造方法私有化
     *
     * @param context
     */
    private YuehuDB(Context context) {
        YuehuOpenHelper helper = new YuehuOpenHelper(context, DB_NAME, null, VERSION);
        sqliteDB = helper.getWritableDatabase();
    }

    public synchronized static YuehuDB getDbInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new YuehuDB(context);
        }
        return dbInstance;
    }

    /**
     * 将文章信息存储到数据库
     */
    public void savePassage(PassageTable passage) {
        if (passage != null) {
            ContentValues values = new ContentValues();
            values.put("passage_title", passage.getPassage_title());
            values.put("passage_url", passage.getPassage_url());
            sqliteDB.insert("passage_table", null, values);
        }
    }

    /**
     * 从数据库中获取文章信息
     */
    public List<PassageTable> queryPassage() {
        List<PassageTable> tables = new ArrayList<>();

        Cursor cursor = sqliteDB.query("passage_table", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                PassageTable table = new PassageTable();
                table.setId(cursor.getInt(cursor.getColumnIndex("id")));
                table.setPassage_title(cursor.getString(cursor.getColumnIndex("passage_title")));
                table.setPassage_url(cursor.getString(cursor.getColumnIndex("passage_url")));
                tables.add(table);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return tables;
    }

    /**
     * 将图片信息存储到数据库
     */
    public void saveImage(ImageTable image) {
        if (image != null) {
            ContentValues values = new ContentValues();
            values.put("image_title", image.getImage_title());
            values.put("image_url", image.getIamge_url());
            sqliteDB.insert("image_table", null, values);
        }
    }

    /**
     * 从数据库中获取文章信息
     */
    public List<ImageTable> queryImage() {
        List<ImageTable> tables = new ArrayList<>();

        Cursor cursor = sqliteDB.query("image_table", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ImageTable table = new ImageTable();
                table.setId(cursor.getInt(cursor.getColumnIndex("id")));
                table.setImage_title(cursor.getString(cursor.getColumnIndex("image_title")));
                table.setIamge_url(cursor.getString(cursor.getColumnIndex("image_url")));
                tables.add(table);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return tables;
    }
}
