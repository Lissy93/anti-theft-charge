package com.aliciasykes.anti_theft_charge

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import com.dx.dxloadingbutton.lib.LoadingButton
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import android.view.Menu
import android.view.MenuItem
import android.content.SharedPreferences
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.widget.TextView
import android.view.ViewGroup
import com.transitionseverywhere.*




class MainActivity : AppCompatActivity() {

    private var armed: Boolean = false // Is the device armed?
    private var prefrences: SharedPreferences? = null // Reference to SharedPreferences
    private val chargingUtil: ChargingUtil = ChargingUtil()

    private lateinit var toggleButton: LoadingButton // Reference to the main toggle button
    private var toggleLastClickTime: Long = 0 // Used to ensure user doesn't accidentally double tap on arm

    private lateinit var armDisarmFunctionality: ArmDisarmFunctionality

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        armDisarmFunctionality = ArmDisarmFunctionality(this)

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

//    val plugInReceiver = object : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//
//            if (action == Intent.ACTION_POWER_CONNECTED) {
//                // Do something when power connected
//            } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
//                // Do something when power disconnected
//            }
//        }
//    }
}
