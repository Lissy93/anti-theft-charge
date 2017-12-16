package com.aliciasykes.anti_theft_charge


class CurrentStatus {
    companion object {
        var isConnected = false // Statically stores connection status
        lateinit var armDisarmFunctionality: ArmDisarmFunctionality // Stores static UI interactions
    }
}

