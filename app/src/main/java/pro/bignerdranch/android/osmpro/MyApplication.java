package pro.bignerdranch.android.osmpro;

/**
 * Created by Севастьян on 18.01.2018.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

public class MyApplication extends Application {
    private static MyApplication singleton;
    public static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    // Возвращает экземпляр данного класса
    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        singleton = this;
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        double X, Y;
        boolean ann;
        X = mSettings.getFloat("x", (float) 2.27);
        Y = mSettings.getFloat("y", (float) 0.5);
        ann = mSettings.getBoolean("smarts", false);
        Note.x = X;
        Note.y = Y;
        NoteSettings.SmartScore = ann;
        Log.i("code", X + " " + Y + " " + ann);
    }
}