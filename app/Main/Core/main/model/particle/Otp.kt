package model.particle

import utils.isValidOtp
import kotlin.jvm.JvmInline

@JvmInline
value class Otp(val otp: String) {
    init {
        check(isValidOtp(otp))
    }
}