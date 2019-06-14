package com.aliciasykes.anti_theft_charge

import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.dx.dxloadingbutton.lib.LoadingButton

class MainActivity : AppCompatActivity() {

    private var preferences: SharedPreferences? = null // Reference to SharedPreferences
    private lateinit var armDisarmFunctionality: ArmDisarmFunctionality
    private lateinit var chargingUtil: ChargingUtil
    private lateinit var toggleButton: LoadingButton // Reference to the main toggle button
    private var toggleLastClickTime: Long = 0 // Used to ensure user doesn't accidentally double tap on arm

    companion object {
        var activityActive = false
    }

    val isActivityActive get() = activityActive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityActive = true

        /* Init the arming/ disarming functionality, and get current status */
        armDisarmFunctionality = ArmDisarmFunctionality(this)
        CurrentStatus.armDisarmFunctionality = armDisarmFunctionality
        chargingUtil = ChargingUtil(applicationContext)


        /* Load app preferences */
        preferences = getSharedPreferences("com.aliciasykes.anti_theft_charge", MODE_PRIVATE)

        /* Get the main toggle button, and call stuff when it is pressed */
        toggleButton = findViewById<View>(R.id.toggleButton) as LoadingButton
        toggleButton.setOnClickListener{
            /* Check that not an accidental double-tap, then toggle arm status */
            if (SystemClock.elapsedRealtime() - toggleLastClickTime > 1000){
                toggleLastClickTime = SystemClock.elapsedRealtime()
                armDisarmFunctionality.toggleDeviceArming()
            }
        }
        toggleButton.isResetAfterFailed = false
        toggleButton.setTextSize(26) // Unfortunately this has to be done programmatically...

        /* Is device plugged in? */
        if(CurrentStatus.isConnected) armDisarmFunctionality.powerConnected()
        else armDisarmFunctionality.powerDisconnected()

        /* Put app into correct (armed/disarmed) state */
        if(CurrentStatus.isArmed) armDisarmFunctionality.armDevice()
        else armDisarmFunctionality.disarmDevice()

        /* Start listening for when the power connector changes */
        startPowerConnectionListener()
    }

    override fun onStop() {
        super.onStop()
        activityActive = false
    }

    override fun onResume() {
        super.onResume()

        /* If this is the first time user has fun the app- show help dialog */
        if (!preferences!!.contains("firstRun")) {
            armDisarmFunctionality.showHelpDialog()
            preferences!!.edit().putBoolean("firstRun", false).apply()
        }

        /* Ensure armDisArmFunctionality, which is a lateint in CurrentStatus has been initialised */
        armDisarmFunctionality = ArmDisarmFunctionality(this)
        CurrentStatus.armDisarmFunctionality = armDisarmFunctionality

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

    /**
     * Determines if a given service (given by class name) is running
     */
    @SuppressWarnings("deprecation") // for ActivityManager.getRunningServices
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    /**
     * Initiates the service, that will listen for power connector changes
     */
    private fun startPowerConnectionListener() {
        val serviceComponent = ComponentName(this, PowerConnectionService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((200)) // wait time
        builder.setOverrideDeadline((200)) // maximum delay
        val jobScheduler = this.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())

        val serviceIntent = Intent(this, serviceComponent::class.java)
        if (!isMyServiceRunning(serviceComponent::class.java)) {
            startService(serviceIntent)
        }
    }

}
