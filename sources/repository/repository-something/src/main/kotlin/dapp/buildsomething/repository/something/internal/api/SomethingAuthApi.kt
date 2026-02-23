package dapp.buildsomething.repository.something.internal.api

import dapp.buildsomething.repository.something.internal.api.model.AuthChallengeResponse
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyRequest
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyResponse
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenRequest
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SomethingAuthApi {

    @GET("api/auth/challenge")
    suspend fun getAuthChallenge(): AuthChallengeResponse

    @POST("api/auth/verify")
    suspend fun verifyAuth(@Body request: AuthVerifyRequest): AuthVerifyResponse

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): RefreshTokenResponse
}