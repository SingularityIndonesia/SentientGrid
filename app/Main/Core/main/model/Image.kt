package model

import model.particle.ImageType
import model.particle.LatLng

data class Image(
    val model: Any,
    val type: ImageType,
    val ratio: Float = 1f,
    val desc: String? = null,
    val timeSignature: Long? = null,
    val locationSignature: LatLng? = null,
) {
    companion object {
        // fun from(drawableRes: DrawableResource): Image {
        //     return Image(
        //         model = drawableRes,
        //         type = ImageType.DrawableResID,
        //         ratio = 1f,
        //     )
        // }

        fun from(url: String): Image {
            return Image(
                model = url,
                type = ImageType.Url,
                ratio = 1f,
            )
        }
    }
}