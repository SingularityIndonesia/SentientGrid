package utils

fun isValidOtp(otp: String): Boolean {
    return otp.length == 6 && runCatching { otp.toInt() }.isSuccess
}