package me.wondertwo.august0802.app;

import android.app.Application;

/**
 * Created by wondertwo on 2016/8/9.
 */
public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public synchronized static App getIns() {
        if (INSTANCE == null) {
            INSTANCE = new App();
        }
        return INSTANCE;
    }
}
