package com.aliciasykes.anti_theft_charge

import android.media.MediaPlayer

class AlarmUtil(_mainActivity: MainActivity) {

    private var mainActivity: MainActivity = _mainActivity
    private lateinit var mp: MediaPlayer

    fun initMp(){
        mp = MediaPlayer.create(mainActivity, R.raw.alarm1)
    }

    fun soundTheAlarm(){
        initMp()
        mp.start()
    }

    fun stopTheAlarm(){
        if (::mp.isInitialized) {
            mp.stop()
        }
    }

}