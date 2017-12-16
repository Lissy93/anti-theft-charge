package com.aliciasykes.anti_theft_charge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dx.dxloadingbutton.lib.LoadingButton
import android.view.Menu
import android.view.MenuItem
import android.content.SharedPreferences
import android.os.SystemClock

class MainActivity : AppCompatActivity() {

    private var prefrences: SharedPreferences? = null // Reference to SharedPreferences

    private lateinit var toggleButton: LoadingButton // Reference to the main toggle button
    private var toggleLastClickTime: Long = 0 // Used to ensure user doesn't accidentally double tap on arm

    private lateinit var armDisarmFunctionality: ArmDisarmFunctionality
    private lateinit var chargingUtil: ChargingUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        armDisarmFunctionality = ArmDisarmFunctionality(this)
        CurrentStatus.armDisarmFunctionality = armDisarmFunctionality
        chargingUtil = ChargingUtil(applicationContext)


        /* Load app preferences */
        prefrences = getSharedPreferences("com.aliciasykes.anti_theft_charge", MODE_PRIVATE)

        /* Get the main toggle button, and call stuff when it is pressed */
        toggleButton = findViewById<View>(R.id.toggleButton) as LoadingButton
        toggleButton.setOnClickListener(View.OnClickListener() {
            /* Check that not an accidental double-tap, then toggle arm status */
            if (SystemClock.elapsedRealtime() - toggleLastClickTime > 1000){
                toggleLastClickTime = SystemClock.elapsedRealtime()
                armDisarmFunctionality.toggleDeviceArming()
            }
        })

        /* Is device plugged in? */
        if(CurrentStatus.isConnected) armDisarmFunctionality.powerConnected()
        else armDisarmFunctionality.powerDisconnected()

        /* Put app into correct (armed/disarmed) state */
        //todo check state
        armDisarmFunctionality.disarmDevice()
    }

    override fun onResume() {
        super.onResume()

        /* If this is the first time user has fun the app- show help dialog */
        if (!prefrences!!.contains("firstRun")) {
            armDisarmFunctionality.showHelpDialog()
            prefrences!!.edit().putBoolean("firstRun", false).apply()
        }
    }

    /**
     * Inflate the options menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }


    /**
     * Specify functionality for each menu item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_arm_disarm -> {
                armDisarmFunctionality.toggleDeviceArming()
                return true
            }
            R.id.menu_help -> {
                armDisarmFunctionality.showHelpDialog()
                return true
            }
            R.id.menu_bug -> {
                // todo
                return true
            }
            R.id.menu_about -> {
                // todo
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
