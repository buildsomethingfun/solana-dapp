package dapp.buildsomething.repository.something.internal.jwt.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class JwtToken(
    val token: String,
    val refreshToken: String,
)