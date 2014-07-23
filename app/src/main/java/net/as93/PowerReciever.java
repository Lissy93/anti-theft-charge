package net.as93;

/**
 * Created by Alicia on 14/06/14.
 */

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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

		KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		boolean showing = kgMgr.inKeyguardRestrictedInputMode();

		if (!isCharging && UserSettings.isEnabled() && UserSettings.isCharge() && showing) {
			playAlarm(context);
			showDialogToStop();
		}
	}


	private void playAlarm(Context c) {
		String tone = UserSettings.getAlarmTone();
		int soundId = R.raw.alarm1;
		if (tone.equals("alarm1")) {
			soundId = R.raw.alarm1;
		} else if (tone.equals("alarm2")) {
			soundId = R.raw.alarm2;
		} else if (tone.equals("alarm3")) {
			soundId = R.raw.alarm3;
		} else if (tone.equals("alarm4")) {
			soundId = R.raw.alarm4;
		}

		UserSettings.setMp(MediaPlayer.create(c, soundId));
		int timeDelaySeconds = UserSettings.getTimeDelay();
		int timeDelay = timeDelaySeconds * 1000;

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				UserSettings.getMp().start();
			}
		}, timeDelay);

		AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);


	}

	public void setAlarmOff() {
		keepPlaying = false;
	}

	public void showDialogToStop() {
		MainActivity ma = new MainActivity();
		ma.showDialogToStop();
	}


}