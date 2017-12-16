package com.aliciasykes.anti_theft_charge


class CurrentStatus {
    companion object {
        var isConnected = false // Statically stores connection status
        var isArmed = false // Statically stores weather the device is armed or not
        lateinit var armDisarmFunctionality: ArmDisarmFunctionality // Stores static UI interactions
    }
}

