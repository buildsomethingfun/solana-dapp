package dapp.buildsomething.feature.auth.di

import dapp.buildsomething.feature.auth.presentation.AuthReducer
import dapp.buildsomething.feature.auth.presentation.AuthStore
import dapp.buildsomething.feature.auth.presentation.AuthStoreProvider
import dapp.buildsomething.feature.auth.presentation.actor.ConnectWalletActor
import dapp.buildsomething.feature.auth.presentation.model.AuthState
import org.koin.dsl.module

val AuthModule = module {
    factory<AuthStoreProvider> {
        object : AuthStoreProvider() {
            override fun provide(): AuthStore {
                return AuthStore(
                    initialEvents = listOf(),
                    initialState = AuthState(),
                    actors = setOf(
                        ConnectWalletActor(
                            authInteractor = get(),
                        ),
                    ),
                    reducer = AuthReducer(),
                )
            }
        }
    }
}
