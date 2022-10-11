package com.alfionte.connectivitychange

import com.alfionte.connectivitychange.models.ConnectivityStatus
import com.alfionte.connectivitychange.models.NetworkSpeed
import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {

    val downstreamBandwidthSpeed: NetworkSpeed.DownloadSpeed?
    val upstreamBandwidthSpeed: NetworkSpeed.UploadSpeed?
    val downstreamBandwidthSpeedChanged: Flow<NetworkSpeed.DownloadSpeed>
    val upstreamBandwidthSpeedChanged: Flow<NetworkSpeed.UploadSpeed>
    val linkProperties: Flow<ConnectivityStatus.LinkPropertiesChanged>
    val networkCapabilities: Flow<ConnectivityStatus.CapabilitiesChanged>
    val isNetworkNotMetered: Flow<Boolean>
    val isConnected: Flow<Boolean>
    val connectivityStatus: Flow<ConnectivityStatus>
}