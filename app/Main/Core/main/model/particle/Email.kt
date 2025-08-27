package model.particle

import utils.isValidEmail
import kotlin.jvm.JvmInline

@JvmInline
value class Email(val email: String) {
    init {
        check(isValidEmail(email))
    }
}