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
    private SharedPreferences settings;
    private Editor editor;


	private static MediaPlayer mp = new MediaPlayer();

    public UserSettings(Context c){
		String appKey = "atc";
        settings = c.getSharedPreferences(appKey, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public String getAlarmTone() {
        return settings.getString("alarmTone","alarm1");
    }

    public void setAlarmTone(String alarmTone) {
        editor.putString("alarmTone",alarmTone);
        editor.commit();
    }

    public int getTimeDelay() {
        return settings.getInt("timeDelay",0);
    }

    public void setTimeDelay(int timeDelay) {
        editor.putInt("timeDelay",timeDelay);
        editor.commit();
    }

    public boolean isEnabled() {
        return settings.getBoolean("isEnabled",false);
    }

    public void setEnabled(boolean enabled) {
       editor.putBoolean("isEnabled",enabled);
       editor.commit();
    }

    public boolean isMovement() {
        return settings.getBoolean("isMovement",false);
    }

    public void setMovement(boolean movement) {
        editor.putBoolean("isMovement",movement);
        editor.commit();
    }

    public boolean isCharge() {
        return settings.getBoolean("isCharge",true);
    }

    public void setCharge(boolean charge) {
        editor.putBoolean("isCharge",charge);
        editor.commit();
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        UserSettings.mp = mp;
    }
}
