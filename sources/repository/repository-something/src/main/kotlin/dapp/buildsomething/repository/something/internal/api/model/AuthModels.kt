package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthChallengeResponse(
    val domain: String,
    val statement: String,
    val nonce: String,
    val issuedAt: String,
    val chainId: String,
)

@Serializable
data class AuthVerifyRequest(
    val input: AuthVerifyInput,
    val output: AuthVerifyOutput,
)

@Serializable
data class AuthVerifyInput(
    val domain: String,
    val statement: String,
    val nonce: String,
    val issuedAt: String,
    val chainId: String,
)

@Serializable
data class AuthVerifyOutputAccount(
    val publicKey: String,
    val address: String,
)

@Serializable
data class AuthVerifyOutput(
    val account: AuthVerifyOutputAccount,
    val signedMessage: String,
    val signature: String,
)

@Serializable
data class AuthVerifyResponse(
    val token: String,
    val refreshToken: String,
    val user: AuthUser,
)

@Serializable
data class AuthUser(
    val id: String,
    val walletAddress: String,
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String,
)

@Serializable
data class RefreshTokenResponse(
    val token: String,
    val refreshToken: String,
    val user: AuthUser,
)
