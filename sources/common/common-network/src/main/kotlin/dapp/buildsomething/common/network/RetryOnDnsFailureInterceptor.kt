package dapp.buildsomething.common.network

import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class RetryOnDnsFailureInterceptor(
    private val maxRetries: Int = 3,
    private val retryDelayMs: Long = 1_000,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: UnknownHostException? = null

        repeat(maxRetries) { attempt ->
            try {
                return chain.proceed(request)
            } catch (e: UnknownHostException) {
                lastException = e
                if (attempt < maxRetries - 1) {
                    Thread.sleep(retryDelayMs)
                }
            }
        }

        throw lastException!!
    }
}
