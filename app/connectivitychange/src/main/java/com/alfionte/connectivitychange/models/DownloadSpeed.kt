package com.alfionte.connectivitychange.models

/**
 * the speed is expressed in Kbps
 */
sealed class NetworkSpeed {
    data class DownloadSpeed(val downloadSpeed: Int) : NetworkSpeed(){
        override fun toString() = downloadSpeed.toString()
    }
    data class UploadSpeed(val uploadSpeed: Int) : NetworkSpeed(){
        override fun toString() = uploadSpeed.toString()
    }
}
