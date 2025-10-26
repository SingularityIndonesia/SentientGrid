package model

import kotlinx.serialization.Serializable
import model.particle.LatLng

@Serializable
data class MapArea(val from: LatLng, val to: LatLng) {
    companion object {
        val ZERO = MapArea(
            LatLng(0.0, 0.0),
            LatLng(0.0, 0.0)
        )
    }
}