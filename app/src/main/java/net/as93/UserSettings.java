package net.as93;

import android.media.MediaPlayer;

/**
 * Created by Alicia on 15/06/14.
 */
public class UserSettings {

    private static String alarmTone = "alarm1";
    private static int timeDelay = 0; // in seconds (0 | 2 | 5 | 10)

    private static boolean enabled = true;
    private static boolean movement = true;
    private static boolean charge = true;

    private static MediaPlayer mp = new MediaPlayer();


    public static String getAlarmTone() {
        return alarmTone;
    }

    public static void setAlarmTone(String alarmTone) {
        UserSettings.alarmTone = alarmTone;
    }

    public static int getTimeDelay() {
        return timeDelay;
    }

    public static void setTimeDelay(int timeDelay) {
        UserSettings.timeDelay = timeDelay;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        UserSettings.enabled = enabled;
    }

    public static boolean isMovement() {
        return movement;
    }

    public static void setMovement(boolean movement) {
        UserSettings.movement = movement;
    }

    public static boolean isCharge() {
        return charge;
    }

    public static void setCharge(boolean charge) {
        UserSettings.charge = charge;
    }

    public static MediaPlayer getMp() {
        return mp;
    }

    public static void setMp(MediaPlayer mp) {
        UserSettings.mp = mp;
    }
}
