package com.alfionte.connectivitychange.extensions

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
fun ConnectivityManager?.hasInternetAccess(network: Network): Boolean {
    val networkCapabilities = this?.getNetworkCapabilities(network) ?: return false
    val hasInternetCapability =
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val hasInternetValidatedCapability =
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

    return hasInternetCapability && hasInternetValidatedCapability
}

fun ConnectivityManager?.hasCapabilityNotMetered(network: Network) = this
    ?.getNetworkCapabilities(network)
    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) == true