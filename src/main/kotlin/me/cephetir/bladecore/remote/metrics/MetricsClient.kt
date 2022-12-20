package me.cephetir.bladecore.remote.metrics

import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.remote.AbstractClient

internal class MetricsClient : AbstractClient("Metrics", BladeCore.metricsUrl) {
    override fun onMessage(message: String) {

    }
}