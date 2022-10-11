package com.alfionte.connectivitychange

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.alfionte.connectivitychange.extensions.hasCapabilityNotMetered
import com.alfionte.connectivitychange.extensions.hasInternetAccess
import com.alfionte.connectivitychange.models.ConnectivityStatus
import com.alfionte.connectivitychange.models.ConnectivityStatus.*
import com.alfionte.connectivitychange.models.NetworkSpeed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConnectivityObserverImpl(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    override val downstreamBandwidthSpeed: NetworkSpeed.DownloadSpeed?
        get() = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.linkDownstreamBandwidthKbps
            ?.let { speed -> NetworkSpeed.DownloadSpeed(speed) }

    override val upstreamBandwidthSpeed: NetworkSpeed.UploadSpeed?
        get() = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.linkUpstreamBandwidthKbps
            ?.let { speed -> NetworkSpeed.UploadSpeed(speed) }

    override val linkProperties: Flow<LinkPropertiesChanged>
        get() = connectivityStatus.filterIsInstance<LinkPropertiesChanged>().distinctUntilChanged()

    override val networkCapabilities: Flow<CapabilitiesChanged>
        get() = connectivityStatus.filterIsInstance<CapabilitiesChanged>().distinctUntilChanged()

    override val downstreamBandwidthSpeedChanged: Flow<NetworkSpeed.DownloadSpeed>
        get() = connectivityStatus
            .filterIsInstance<CapabilitiesChanged>()
            .map { NetworkSpeed.DownloadSpeed(it.networkCapabilities.linkDownstreamBandwidthKbps) }
            .distinctUntilChanged()

    override val upstreamBandwidthSpeedChanged: Flow<NetworkSpeed.UploadSpeed>
        get() = connectivityStatus
            .filterIsInstance<CapabilitiesChanged>()
            .map { NetworkSpeed.UploadSpeed(it.networkCapabilities.linkUpstreamBandwidthKbps) }
            .distinctUntilChanged()

    override val isNetworkNotMetered: Flow<Boolean>
        get() = networkCapabilities.map { connectivityManager.hasCapabilityNotMetered(it.network) }

    override val isConnected: Flow<Boolean>
        get() = connectivityStatus.map {
            when (it) {
                is Available -> connectivityManager.hasInternetAccess(it.network)
                is CapabilitiesChanged -> connectivityManager.hasInternetAccess(it.network)
                is BlockedStatusChanged -> connectivityManager.hasInternetAccess(it.network)
                is LinkPropertiesChanged -> connectivityManager.hasInternetAccess(it.network)
                is Loosing -> false
                is Lost -> false
                Unavailable -> false
            }
        }

    override val connectivityStatus: Flow<ConnectivityStatus>
        get() = callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(Available(network)) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(Loosing(network, maxMsToLive)) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(Lost(network)) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(Unavailable) }
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    launch { send(CapabilitiesChanged(network, networkCapabilities)) }
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    super.onBlockedStatusChanged(network, blocked)
                    launch { send(BlockedStatusChanged(network, blocked)) }
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    super.onLinkPropertiesChanged(network, linkProperties)
                    launch { send(LinkPropertiesChanged(network, linkProperties)) }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
}