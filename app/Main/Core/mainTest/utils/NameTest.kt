package utils

import stdextra.shouldBe
import kotlin.test.Test

class NameTest {
    @Test
    fun `test nickname`() {
        "Dahyun".initialName() shouldBe "D"
        "Cute Dahyun".initialName() shouldBe "CD"
    }
}