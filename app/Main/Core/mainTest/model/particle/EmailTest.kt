package model.particle

import stdextra.Fail
import stdextra.Success
import stdextra.must
import kotlin.test.Test

class EmailTest {
    @Test()
    fun `valid email`() {
        { Email("some.email@somehwere.com") } must Success
    }

    @Test()
    fun `invalid email`() {
        { Email("ssdsfsfsf") } must Fail
    }
}