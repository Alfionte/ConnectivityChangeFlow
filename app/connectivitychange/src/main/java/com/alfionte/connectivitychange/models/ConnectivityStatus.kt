package com.alfionte.connectivitychange.models

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

sealed class ConnectivityStatus {
    data class Available(
        val network: Network
    ) : ConnectivityStatus()

    data class Loosing(
        val network: Network,
        val maxMsToLive: Int,
    ) : ConnectivityStatus()

    data class Lost(
        val network: Network
    ) : ConnectivityStatus()

    data class CapabilitiesChanged(
        val network: Network,
        val networkCapabilities: NetworkCapabilities
    ) : ConnectivityStatus()

    data class BlockedStatusChanged(
        val network: Network,
        val isBlocked: Boolean
    ) : ConnectivityStatus()

    data class LinkPropertiesChanged(
        val network: Network,
        val linkProperties: LinkProperties
    ) : ConnectivityStatus()

    object Unavailable : ConnectivityStatus() {
        override fun toString() = "Unavailable"
    }
}