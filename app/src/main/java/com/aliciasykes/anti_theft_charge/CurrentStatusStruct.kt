package com.aliciasykes.anti_theft_charge


class CurrentStatus {
    companion object {
        var isConnected = false // Statically stores connection status
        var isArmed = false // Statically stores weather the device is armed or not
        var isUnderAttack = false // Statically stores weather we're under enemy fire
        lateinit var armDisarmFunctionality: ArmDisarmFunctionality // Stores static UI interactions
    }
}

