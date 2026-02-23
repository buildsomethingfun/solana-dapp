package dapp.buildsomething.repository.something.internal.jwt

import okhttp3.Interceptor
import okhttp3.Response

internal class JwtInterceptor(
    private val tokenProvider: JwtRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()

        return chain.request()
            .newBuilder()
            .header("Authorization", "Bearer ${token?.token}")
            .build()
            .let(chain::proceed)
    }
}