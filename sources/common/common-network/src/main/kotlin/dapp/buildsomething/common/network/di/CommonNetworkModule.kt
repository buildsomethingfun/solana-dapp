package dapp.buildsomething.common.network.di

import dapp.buildsomething.common.network.connection.NetworkConnectionObserver
import dapp.buildsomething.common.network.connection.NetworkConnectionObserverImpl
import dapp.buildsomething.common.network.ip.UserIPProvider
import dapp.buildsomething.common.network.ip.UserIPProviderImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val CommonNetworkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    single<NetworkConnectionObserver> {
        NetworkConnectionObserverImpl(
            context = get()
        )
    }

    single<UserIPProvider> {
        UserIPProviderImpl()
    }
}