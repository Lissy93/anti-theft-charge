package com.aliciasykes.anti_theft_charge

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import com.dx.dxloadingbutton.lib.LoadingButton

class MainActivity : AppCompatActivity() {

    private var armed: Boolean = false
    private lateinit var toggleButton: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Get the main toggle button, and call stuff when it is pressed */
        toggleButton = findViewById<View>(R.id.toggleButton) as LoadingButton
        toggleButton.setOnClickListener(View.OnClickListener() {
            this.toggleDeviceArming()
        })
    }

    /**
     * Calls to arm the device if it is disarmed, and disarms if armed
     */
    private fun toggleDeviceArming(){
        if (this.armed) this.disarmDevice() else this.armDevice()
    }

    /**
     * Arms the device
     * Updates UI and calls appropriate methods
     */
    private fun armDevice(){
        toggleButton.startLoading()
        Handler().postDelayed({
            toggleButton.loadingSuccessful()
            this.updateBackgroundColor(R.color.colorSafe)
        }, 500)
        armed = true
    }

    /**
     * (you guessed it!) Disarms the device
     * Also resetting the UI and removing listener
     */
    private fun disarmDevice(){
        toggleButton.reset()
        this.updateBackgroundColor()
        armed = false
    }

    private fun updateBackgroundColor( newBgColor: Int = R.color.colorNeutral){
        val mainLayout = findViewById<View>(R.id.mainLayout)
        mainLayout.setBackgroundColor(ContextCompat.getColor(this, newBgColor))
    }
}
