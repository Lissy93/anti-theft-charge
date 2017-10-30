package com.aliciasykes.anti_theft_charge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dx.dxloadingbutton.lib.LoadingButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lb = findViewById<View>(R.id.toggleButton) as LoadingButton
        lb.setOnClickListener(View.OnClickListener() {

            fun onClick(view: View) {
                lb.startLoading() //start loading
            }
        })



    }
}
