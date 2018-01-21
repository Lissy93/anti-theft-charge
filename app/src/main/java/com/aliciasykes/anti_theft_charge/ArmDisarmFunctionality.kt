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

class ArmDisarmFunctionality(_mainActivity: MainActivity) {

    private var toggleButton: LoadingButton
    private var mainActivity: MainActivity = _mainActivity

    init {
        toggleButton = mainActivity.findViewById<View>(R.id.toggleButton) as LoadingButton
    }

    /**
     * Calls to arm the device if it is disarmed, and disarms if armed
     */
    fun toggleDeviceArming(){
        if (CurrentStatus.isArmed) disarmDevice() else armDevice()
    }

    /**
     * Called by the event emitter when the power is disconnected
     * Determins what state the device is in, and takes appropriate action
     */
    fun powerDisconnected(){
        if(CurrentStatus.isArmed){ // Armed device has just been disconnected
            deviceIsUnderAttack()
        }
        else{ // Unarmed when disconnected, put back into ready state
            updateStatusLabel(makeStatusLabelText(false))
            updateBackgroundColor(R.color.colorNeutral)
        }
    }

    /**
     * Called when the power cable is reconnected
     * Calls to update the UI accordingly
     */
    fun powerConnected(){

        if (!CurrentStatus.isArmed){ // Is armed, and now is connected - set green
            updateBackgroundColor(R.color.colorAccent) // Set bg color
        }

        if(!CurrentStatus.isUnderAttack){ // Not under attack, set home text
            updateStatusLabel(makeStatusLabelText(CurrentStatus.isArmed))
        }
        else{ // Device was under active enemy attack - but power has just been reconnected
            armDevice()
            showSnackMessage("Power has been reconnected. Device is still Armed.")
            deviceNoLongerUnderAttack()
        }
    }

    /**
     * Arms the device
     * Updates UI and calls appropriate methods
     */
    fun armDevice(){
        if(!CurrentStatus.isConnected){ // NOT charging, show message and exit func
            showSnackMessage("Plug device in first", " Explain More ", ::showHelpDialog)
        }
        else{ // Device is charging! So proceed to arming
            toggleButton.startLoading()
            Handler().postDelayed({
                toggleButton.loadingSuccessful()
                updateBackgroundColor(R.color.colorSafe) // Set bg color
                updateStatusLabel(makeStatusLabelText(true)) // Update status text
            }, 500)
            CurrentStatus.isArmed = true
        }
    }

    /**
     * (you guessed it!) Disarms the device
     * Also resetting the UI and removing listener
     */
    fun disarmDevice(){
        toggleButton.reset()
        val bgCol = if (CurrentStatus.isConnected) R.color.colorAccent else R.color.colorNeutral
        updateBackgroundColor(bgCol)
        updateStatusLabel(makeStatusLabelText(false))
        CurrentStatus.isArmed = false
    }

    /**
     * Will be called when device gets unplugged
     * from power while it is armed and locked
     * Updates text, changes background and sounds alarm
     */
    private fun deviceIsUnderAttack() {

        CurrentStatus.isUnderAttack = true

        updateBackgroundColor(R.color.colorNearlyDanger)
        updateStatusLabel("Device under Attack.\n Alarm will sound unless plugged back in or dismissed")
        toggleButton.reset()
        toggleButton.setText("Dismiss")

        Handler().postDelayed({
            toggleButton.startLoading()
            updateBackgroundColor(R.color.colorDanger)
            updateStatusLabel("Device under Attack. \nUnlock with your pass code, then tap here")
            Handler().postDelayed({
                toggleButton.loadingFailed()
            }, 2000)
        }, 2000)
    }

    /**
     * Called when the device has either just been secured
     * or the cable has been reconnected
     * Will silence the alarm, reset notification
     */
    private fun deviceNoLongerUnderAttack() {
        // todo
    }

    /**
     * Returns a string, which will then be used as the label below button
     * Takes an optional param of armed
     */
    private fun makeStatusLabelText( armed: Boolean = false ): String {
        return if (armed){
            mainActivity.getString(R.string.status_label_armed)
        }
        else {
            mainActivity.getString(
                    if (CurrentStatus.isConnected)
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
    private fun updateBackgroundColor( newBgColorId: Int = R.color.colorAccent, oldBgColorId: Int = 0, bgFadeDuration: Long = 1000){
        val mainLayout = mainActivity.findViewById<View>(R.id.mainLayout)
        val newBgColor = ContextCompat.getColor(mainActivity, newBgColorId)

        val oldBgColor = if (oldBgColorId != 0) {
            ContextCompat.getColor(mainActivity, newBgColorId)
        }
        else {
            if (mainLayout.background is ColorDrawable)
                (mainLayout.background as ColorDrawable).color
            else Color.TRANSPARENT
        }

        val colorFade = ObjectAnimator.ofObject(
                mainLayout, "backgroundColor", ArgbEvaluator(), oldBgColor, newBgColor)
        colorFade.duration = bgFadeDuration
        colorFade.start()
        toggleButton.setTextColor(newBgColor) // Button text always the same as background color
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
    fun showHelpDialog(){
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