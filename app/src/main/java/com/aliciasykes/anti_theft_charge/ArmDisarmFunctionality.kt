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
        determineAndSetState()
    }

    /**
     * Called when the power cable is reconnected
     * Calls to update the UI accordingly
     */
    fun powerConnected(){
        determineAndSetState()
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
                CurrentStatus.isArmed = true
                setArmedState()
            }, 500)
        }
    }

    /**
     * (you guessed it!) Disarms the device
     * Also resetting the UI and removing listener
     */
    fun disarmDevice(){
        CurrentStatus.isArmed = false
        determineAndSetState()
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
        toggleButton.setText("Stop Alarm")

        Handler().postDelayed({
            toggleButton.startLoading()
            Handler().postDelayed({
                setAttackState()
            }, 2000)
        }, 2000)
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
        val statusLabel: TextView = mainActivity.findViewById<TextView>(R.id.status_label)
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

    private fun determineAndSetState(){
        val armed = CurrentStatus.isArmed
        val charging = CurrentStatus.isConnected

        if(armed && charging){
            setArmedState()
        }
        else if(armed && !charging){
            setAttackState()
        }
        else if(!armed && charging){
            setReadyState()
        }
        else if(!armed && !charging){
            setNeutralState()
        }
    }

    private fun setReadyState(){
        toggleButton.reset()
        updateBackgroundColor(R.color.colorAccent)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_protect))
        updateStatusLabel(
                mainActivity.getString(R.string.status_label_charging)+"\n"+
                mainActivity.getString(R.string.status_label_unarmed)
        )
    }

    private fun setArmedState(){
        updateBackgroundColor(R.color.colorSafe)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_disarm))
        toggleButton.loadingSuccessful()
        updateStatusLabel(mainActivity.getString(R.string.status_label_armed))
    }

    private fun setNeutralState(){
        toggleButton.reset()
        updateBackgroundColor(R.color.colorNeutral)
        toggleButton.setText(mainActivity.getString(R.string.btn_plug_in_tap_here))
        updateStatusLabel(
                mainActivity.getString(R.string.status_label_not_charging)+"\n"+
                mainActivity.getString(R.string.status_label_unarmed)
        )
    }

    private fun setAttackState(){
        updateBackgroundColor(R.color.colorDanger)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_secure))
        toggleButton.loadingFailed()
        updateStatusLabel(mainActivity.getString(R.string.status_label_under_attack))
    }


}