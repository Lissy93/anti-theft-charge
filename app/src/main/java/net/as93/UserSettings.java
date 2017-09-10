package net.as93;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;

/**
 * Created by Alicia on 15/06/14.
 */
public class UserSettings {

    /* DEFAULTS */
    private static SharedPreferences settings;
    private static Editor editor;
    private static String appKey;


    private static MediaPlayer mp = new MediaPlayer();

    public UserSettings(Context c){
        appKey = "atc";
        settings = c.getSharedPreferences(appKey, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static String getAlarmTone() {
        return settings.getString("alarmTone","alarm1");
    }

    public static void setAlarmTone(String alarmTone) {
        editor.putString("alarmTone",alarmTone);
        editor.commit();
    }

    public static int getTimeDelay() {
        return settings.getInt("timeDelay",0);
    }

    public static void setTimeDelay(int timeDelay) {
        editor.putInt("timeDelay",timeDelay);
        editor.commit();
    }

    public static boolean isEnabled() {
        return settings.getBoolean("isEnabled",false);
    }

    public static void setEnabled(boolean enabled) {
       editor.putBoolean("isEnabled",enabled);
       editor.commit();
    }

    public static boolean isMovement() {
        return settings.getBoolean("isMovement",false);
    }

    public static void setMovement(boolean movement) {
        editor.putBoolean("isMovement",movement);
        editor.commit();
    }

    public static boolean isCharge() {
        return settings.getBoolean("isCharge",true);
    }

    public static void setCharge(boolean charge) {
        editor.putBoolean("isCharge",charge);
        editor.commit();
    }

    public static MediaPlayer getMp() {
        return mp;
    }

    public static void setMp(MediaPlayer mp) {
        UserSettings.mp = mp;
    }
}
