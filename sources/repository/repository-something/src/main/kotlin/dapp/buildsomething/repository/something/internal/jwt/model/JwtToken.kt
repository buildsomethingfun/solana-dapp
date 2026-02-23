package dapp.buildsomething.repository.something.internal.jwt.model

internal data class JwtToken(
    val token: String,
    val refreshToken: String,
)