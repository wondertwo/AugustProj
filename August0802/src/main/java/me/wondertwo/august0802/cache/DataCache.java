package me.wondertwo.august0802.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import me.wondertwo.august0802.app.App;
import me.wondertwo.august0802.bean.girl.GirlItem;

/**
 * Created by wondertwo on 2016/8/3.
 */
public class DataCache {

    private static String DATA_FILE_NAME = "data_girl.db";

    private static DataCache INSTANCE;
    File dataFile = new File(App.getIns().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    private DataCache() {

    }

    public static DataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    public List<GirlItem> readItems() {
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<GirlItem>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(List<GirlItem> girlItems) {
        String json = gson.toJson(girlItems);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItems() {
        dataFile.delete();
    }
}
