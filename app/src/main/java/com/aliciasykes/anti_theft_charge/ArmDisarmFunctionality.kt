package com.aliciasykes.anti_theft_charge

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dx.dxloadingbutton.lib.LoadingButton
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager

class ArmDisarmFunctionality {

    private lateinit var toggleButton: LoadingButton
    private lateinit var mainActivity: MainActivity


    constructor(_mainActivity: MainActivity){
        mainActivity = _mainActivity
        toggleButton = mainActivity.findViewById<View>(R.id.toggleButton) as LoadingButton
    }

    private var armed: Boolean = false
    private var chargingUtil: ChargingUtil = ChargingUtil()


    /**
     * Calls to arm the device if it is disarmed, and disarms if armed
     */
    public fun toggleDeviceArming(){
        if (armed) disarmDevice() else armDevice()
    }

    /**
     * Arms the device
     * Updates UI and calls appropriate methods
     */
    public fun armDevice(){
        if(!chargingUtil.isConnected(mainActivity.applicationContext)){ // NOT charging, show message and exit func
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
    public fun disarmDevice(){
        toggleButton.reset()
        updateBackgroundColor()
        updateStatusLabel(makeStatusLabelText(false))
        armed = false
    }

    private fun makeStatusLabelText( armed: Boolean = false ): String {
        if (armed){
            return mainActivity.getString(R.string.status_label_armed)
        }
        else {
            return mainActivity.getString(
                    if (chargingUtil.isConnected(mainActivity.applicationContext))
                        R.string.status_label_charging
                    else R.string.status_label_not_charging
            ) +
                    "\n" +
                    mainActivity.getString(R.string.status_label_prefix) +
                    " " +
                    mainActivity.getString(R.string.status_label_unarmed)
        }
    }

    /**
     * Updates the background color of main layout
     * With a nice fade animation of by default 1 second
     */
    private fun updateBackgroundColor( newBgColorId: Int = R.color.colorAccent){
        val mainLayout = mainActivity.findViewById<View>(R.id.mainLayout)
        val bgFadeDuration: Long = 1000
        val newBgColor = ContextCompat.getColor(mainActivity, newBgColorId)
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
        val statusLabel: TextView = mainActivity.findViewById(R.id.status_label)
        val transitionsContainer = mainActivity.findViewById<ViewGroup>(R.id.mainLayout)

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
    public fun showHelpDialog(){
        MaterialStyledDialog.Builder(mainActivity)
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
        val mainLayout = mainActivity.findViewById<View>(R.id.mainLayout)
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG)
                .setAction(buttonText, View.OnClickListener { buttonFunction() })
                .show()
    }

}