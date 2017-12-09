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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Load app preferences */
        prefrences = getSharedPreferences("com.aliciasykes.anti_theft_charge", MODE_PRIVATE)

        /* Get the main toggle button, and call stuff when it is pressed */
        toggleButton = findViewById<View>(R.id.toggleButton) as LoadingButton
        toggleButton.setOnClickListener(View.OnClickListener() {
            /* Check that not an accidental double-tap, then toggle arm status */
            if (SystemClock.elapsedRealtime() - toggleLastClickTime > 1000){
                toggleLastClickTime = SystemClock.elapsedRealtime()
                toggleDeviceArming()
            }
        })

        /* Put app into correct (armed/disarmed) state */
        //todo check state
        disarmDevice()
    }

    override fun onResume() {
        super.onResume()

        /* If this is the first time user has fun the app- show help dialog */
        if (!prefrences!!.contains("firstRun")) {
            showHelpDialog()
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
     * Calls to arm the device if it is disarmed, and disarms if armed
     */
    private fun toggleDeviceArming(){
        if (armed) disarmDevice() else armDevice()
    }

    /**
     * Arms the device
     * Updates UI and calls appropriate methods
     */
    private fun armDevice(){
        if(!chargingUtil.isConnected(applicationContext)){ // NOT charging, show message and exit func
            showSnackMessage("Plug device in first", " Explain More ", ::showHelpDialog)
        }
        else{ // Device is charging! So proceed to arming
            toggleButton.startLoading()
            Handler().postDelayed({
                toggleButton.loadingSuccessful()
                updateBackgroundColor(R.color.colorSafe) // Set bg color
                updateStatusLabel(makeStatusLabelText(true)) // Update status text
            }, 500)
            armed = true
        }
    }

    /**
     * (you guessed it!) Disarms the device
     * Also resetting the UI and removing listener
     */
    private fun disarmDevice(){
        toggleButton.reset()
        updateBackgroundColor()
        updateStatusLabel(makeStatusLabelText(false))
        armed = false
    }

    private fun makeStatusLabelText( armed: Boolean = false ): String {
        if (armed){
            return getString(R.string.status_label_armed)
        }
        else {
            return getString(
                if (chargingUtil.isConnected(applicationContext))
                    R.string.status_label_charging
                else R.string.status_label_not_charging
                ) +
                "\n" +
                getString(R.string.status_label_prefix) +
                " " +
                getString(R.string.status_label_unarmed)
        }
    }

    /**
     * Updates the background color of main layout
     * With a nice fade animation of by default 1 second
     */
    private fun updateBackgroundColor( newBgColorId: Int = R.color.colorAccent){
        val mainLayout = findViewById<View>(R.id.mainLayout)
        val bgFadeDuration: Long = 1000
        val newBgColor = ContextCompat.getColor(this, newBgColorId)
        val oldBgColor =
                if (mainLayout.background is ColorDrawable)
                    (mainLayout.background as ColorDrawable).color
                else Color.TRANSPARENT

        val colorFade = ObjectAnimator.ofObject(
                mainLayout, "backgroundColor", ArgbEvaluator(), oldBgColor, newBgColor)
        colorFade.duration = bgFadeDuration
        colorFade.start()
    }

    /**
     * Updates the status label, to make it clear if the device is armed or not
     * Uses a nice animation, thanks to @andkulikov for creating Transitions-Everywhere
     */
    private fun updateStatusLabel(newTextValue: String = ""){

        /* Get reference to the status label */
        val statusLabel: TextView = findViewById(R.id.status_label)
        val transitionsContainer = findViewById<ViewGroup>(R.id.mainLayout)

        /* Set up the animation configuration */
        TransitionManager.beginDelayedTransition(
                transitionsContainer,
                ChangeText()
                    .setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN)
                    .setDuration(500))

        /* Update text*/
        statusLabel.text = newTextValue
    }

    /**
     * Displays the "How to Use" dialog
     */
    private fun showHelpDialog(){
        MaterialStyledDialog.Builder(this)
                .setTitle("How to Use")
                .setDescription("Lorem Ipsum dolor sit ammet")
                .setScrollable(true)
                .setIcon(R.drawable.icon)
                .setCancelable(true)
                .setPositiveText("Got it!")
                .show()
    }

    /**
     * Displays the SnackBar at the bottom of the main layout with specified text
     * Optionally, also specify button text and function to execute on tap
     */
    private fun showSnackMessage(message: String,
                                 buttonText: String = "",  buttonFunction: () -> Unit = fun (){}){
        val mainLayout = findViewById<View>(R.id.mainLayout)
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG)
                .setAction(buttonText, View.OnClickListener { buttonFunction() })
                .show()
    }

    /**
     * Specify functionality for each menu item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_arm_disarm -> {
                toggleDeviceArming()
                return true
            }
            R.id.menu_help -> {
                showHelpDialog()
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
