package dapp.buildsomething.repository.something.internal.jwt

import dapp.buildsomething.repository.something.internal.api.SomethingAuthApi
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenRequest
import dapp.buildsomething.repository.something.internal.jwt.model.JwtToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class JwtAuthenticator(
    private val tokenProvider: JwtRepository,
    private val api: SomethingAuthApi,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val authToken = tokenProvider.getToken() ?: return null

        val result = runCatching {
            runBlocking {
                api.refreshToken(RefreshTokenRequest(authToken.refreshToken))
            }
        }.getOrNull() ?: return null

        // Token was cleared during refresh (logout happened), don't restore it
        if (tokenProvider.getToken() == null) return null

        tokenProvider.setToken(JwtToken(result.token, result.refreshToken))

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${result.token}")
            .build()
    }
}
