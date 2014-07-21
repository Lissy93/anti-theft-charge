package net.as93;

/**
 * Created by Alicia on 14/06/14.
 */
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;

public class PowerReciever extends BroadcastReceiver {

    private static boolean keepPlaying = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        UserSettings us = new UserSettings();

        KeyguardManager kgMgr =  (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean showing = kgMgr.inKeyguardRestrictedInputMode();

        if(!isCharging && us.isEnabled() && us.isCharge() && showing){
            playAlarm(context);
            showDialogToStop();
        }
    }




    private void playAlarm(Context c){
        UserSettings us = new UserSettings();
        String tone = us.getAlarmTone();
        int soundId = R.raw.alarm1;
        if(tone=="alarm1"){soundId = R.raw.alarm1; } else if(tone=="alarm2"){soundId = R.raw.alarm2; }
        else if(tone=="alarm3"){soundId = R.raw.alarm3; } else if(tone=="alarm4"){soundId = R.raw.alarm4; }

        us.setMp(MediaPlayer.create(c, soundId));
        int timeDelaySeconds = us.getTimeDelay();
        int timeDelay = timeDelaySeconds * 1000;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                UserSettings us = new UserSettings();
                us.getMp().start();
                   }
        }, timeDelay);


        AudioManager audioManager = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);



    }

    public void setAlarmOff(){
        keepPlaying = false;
    }

    public void showDialogToStop(){
        MainActivity ma = new MainActivity();
        ma.showDialogToStop();
    }




}