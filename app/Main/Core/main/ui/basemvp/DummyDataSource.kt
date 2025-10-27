package ui.basemvp

import model.particle.AuthenticationToken

class DummyDataSource {
    suspend fun submitLogin(email: String, password: String): Result<AuthenticationToken> = runCatching {
        TODO()
    }
}