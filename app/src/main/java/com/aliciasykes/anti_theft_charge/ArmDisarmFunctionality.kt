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
    private var au: AlarmUtil = AlarmUtil(mainActivity)
    private var nu: NotificationUtil = NotificationUtil(mainActivity)

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
        if(CurrentStatus.isArmed){
            deviceIsUnderAttack()
        }
        else{
            determineAndSetState()
        }
    }

    /**
     * Called when the power cable is reconnected
     * Calls to update the UI accordingly
     */
    fun powerConnected(){
        if(CurrentStatus.isArmed) {
            armDevice()
        }
        else{
            determineAndSetState()
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
            au.stopTheAlarm()
            toggleButton.startLoading()
            Handler().postDelayed({
                CurrentStatus.isArmed = true
                setArmedState()

                /* Show the notification */
                nu.showNotification()

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

        /* Initial UI warning state */
        updateBackgroundColor(R.color.colorNearlyDanger)
        updateStatusLabel(mainActivity.getString(R.string.status_label_almost_under_attack))
        toggleButton.reset()
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_secure))

        /* Sound the alarm */
        au.soundTheAlarm()

        /* Show the notification */
        nu.showNotification()

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
                .setAction(buttonText, { buttonFunction() })
                .show()
    }

    /**
     * Based on weather the device is plugged in/ not plugged in
     * and armed/ no armed, it calls the correct method to update the
     * button and label text as well as colours
     */
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

    /**
     * Device is plugged in, but not armed
     */
    private fun setReadyState(){
        toggleButton.reset()
        updateBackgroundColor(R.color.colorAccent)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_protect))
        updateStatusLabel(
                mainActivity.getString(R.string.status_label_charging)+"\n"+
                mainActivity.getString(R.string.status_label_unarmed)
        )
    }

    /**
     * Device is plugged in, and is armed
     */
    private fun setArmedState(){
        updateBackgroundColor(R.color.colorSafe)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_disarm))
        toggleButton.loadingSuccessful()
        updateStatusLabel(mainActivity.getString(R.string.status_label_armed))
    }

    /**
     * Device is neither plugged in, nor armed
     */
    private fun setNeutralState(){
        toggleButton.reset()
        updateBackgroundColor(R.color.colorNeutral)
        toggleButton.setText(mainActivity.getString(R.string.btn_plug_in_tap_here))
        updateStatusLabel(
                mainActivity.getString(R.string.status_label_not_charging)+"\n"+
                mainActivity.getString(R.string.status_label_unarmed)
        )
    }

    /**
     * Device is not plugged in, but is armed
     */
    private fun setAttackState(){
        toggleButton.reset()
        updateBackgroundColor(R.color.colorDanger)
        toggleButton.setText(mainActivity.getString(R.string.btn_tap_to_secure))
        toggleButton.loadingFailed()
        updateStatusLabel(mainActivity.getString(R.string.status_label_under_attack))
    }


}