package model.particle

import stdextra.Fail
import stdextra.Success
import stdextra.must
import kotlin.test.Test

class OtpTest {
    @Test()
    fun `valid otp`() {
        { Otp("123456") } must Success
    }

    @Test()
    fun `invalid otp`() {
        { Otp("112") } must Fail
        { Otp("112 345") } must Fail
        { Otp("11211a") } must Fail
        { Otp("asdad") } must Fail
    }
}