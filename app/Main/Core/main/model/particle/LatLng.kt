package model.particle

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class LatLng(val latLng: Pair<Double, Double>)
