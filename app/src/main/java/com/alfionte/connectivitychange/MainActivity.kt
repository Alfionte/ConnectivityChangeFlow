package com.alfionte.connectivitychange

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alfionte.connectivitychange.composables.Label
import com.alfionte.connectivitychange.ui.theme.ConnectivityChangeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val connectivityObserver: ConnectivityObserver =
            ConnectivityObserverImpl(connectivityManager)

        setContent {
            val downstreamBandwidthSpeed by connectivityObserver
                .downstreamBandwidthSpeedChanged
                .collectAsState(initial = 0)

            val upstreamBandwidthSpeed by connectivityObserver
                .upstreamBandwidthSpeedChanged
                .collectAsState(initial = 0)

            val isConnected by connectivityObserver
                .isConnected
                .collectAsState(initial = false)

            val isNetworkNotMetered by connectivityObserver
                .isNetworkNotMetered
                .collectAsState(initial = false)

            ConnectivityChangeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val connectionColor = if (isConnected) colors.primary else colors.primaryVariant
                        val meteredColor = if (isNetworkNotMetered) colors.primary else colors.primaryVariant
                        val statusColor = colors.secondary

                        Label(connectionColor, "Network is connected: $isConnected")
                        Spacer(modifier = Modifier.height(16.dp))
                        Label(meteredColor, "Network is not metered: $isNetworkNotMetered")
                        Spacer(modifier = Modifier.height(16.dp))
                        Label(statusColor, "Download: $downstreamBandwidthSpeed")
                        Spacer(modifier = Modifier.height(16.dp))
                        Label(statusColor, "Upload: $upstreamBandwidthSpeed")
                    }
                }
            }
        }
    }
}