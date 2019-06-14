package com.aliciasykes.anti_theft_charge

import android.media.MediaPlayer
import android.media.AudioManager
import android.content.Context.AUDIO_SERVICE

class AlarmUtil(_mainActivity: MainActivity) {

    private var mainActivity: MainActivity = _mainActivity
    private lateinit var mp: MediaPlayer
    private lateinit var am: AudioManager
    private var previousVolumeLevel: Int = 0
    companion object {
        var alarmIsSounding = false
    }

    /**
     * BEEP BEEP BEEP BEEP BEEP BEEP....
     */
    fun soundTheAlarm(){
        if(!alarmIsSounding) {
            mp = MediaPlayer.create(mainActivity, R.raw.alarm1)
            setVolumeToMax()
            mp.start()
            alarmIsSounding = true
        }
    }

    /**
     * Ah, silence!
     */
    fun stopTheAlarm(){
        alarmIsSounding = false
        if (::mp.isInitialized){
            mp.stop()
        }
        setVolumeBackToDefault()
    }

    /**
     * Sets the device media volume to max, when the alarm starts
     * Also records what the previous volume was, so we can put it back
     */
    private fun setVolumeToMax(){
        am = mainActivity.getSystemService(AUDIO_SERVICE) as AudioManager
        previousVolumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0)
    }

    /**
     * Puts device back to previous volume, when alarm stops
     */
    private fun setVolumeBackToDefault(){
        if (previousVolumeLevel != 0 && ::am.isInitialized){
            am.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolumeLevel, 0)
        }

    }
}