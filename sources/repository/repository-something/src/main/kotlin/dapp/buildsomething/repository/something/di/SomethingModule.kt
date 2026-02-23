package dapp.buildsomething.repository.something.di

import dapp.buildsomething.common.network.backendurl.BackendUrl
import dapp.buildsomething.repository.something.interactor.AuthInteractor
import dapp.buildsomething.repository.something.interactor.ChatInteractor
import dapp.buildsomething.repository.something.internal.interactor.AuthInteractorImpl
import dapp.buildsomething.repository.something.internal.interactor.ChatInteractorImpl
import dapp.buildsomething.repository.something.internal.api.SomethingApi
import dapp.buildsomething.repository.something.internal.api.SomethingAuthApi
import dapp.buildsomething.repository.something.internal.stream.UiMessageStreamParser
import dapp.buildsomething.repository.something.internal.jwt.JwtRepository
import dapp.buildsomething.repository.something.internal.jwt.JwtRepositoryImpl
import dapp.buildsomething.repository.something.internal.jwt.JwtAuthenticator
import dapp.buildsomething.repository.something.internal.jwt.JwtInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val SomethingModule = module {
    single<JwtRepository> {
        JwtRepositoryImpl(appPreferences = get())
    }

    single<AuthInteractor> {
        AuthInteractorImpl(
            api = get(),
            walletRepository = get(),
            userRepository = get(),
            jwtRepository = get(),
        )
    }

    single<ChatInteractor> {
        val json = get<Json>(qualifier = StringQualifier("AppJson"))
        ChatInteractorImpl(
            api = get(),
            streamParser = UiMessageStreamParser(json = json),
        )
    }

    single<SomethingAuthApi> {
        val json = get<Json>(qualifier = StringQualifier("AppJson"))

        Retrofit.Builder()
            .baseUrl(BackendUrl.Environmental.app)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SomethingAuthApi::class.java)
    }

    single<SomethingApi> {
        val json = get<Json>(qualifier = StringQualifier("AppJson"))
        val client = get<OkHttpClient>()
            .newBuilder()
            .addInterceptor(JwtInterceptor(tokenProvider = get()))
            .authenticator(JwtAuthenticator(tokenProvider = get(), api = get()))
            .build()

        Retrofit.Builder()
            .baseUrl(BackendUrl.Environmental.app)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SomethingApi::class.java)
    }
}
