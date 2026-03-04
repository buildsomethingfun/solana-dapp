package dapp.buildsomething.feature.splash.di

import dapp.buildsomething.feature.splash.presentation.SplashReducer
import dapp.buildsomething.feature.splash.presentation.SplashStore
import dapp.buildsomething.feature.splash.presentation.SplashStoreProvider
import dapp.buildsomething.feature.splash.presentation.actor.CheckOnboardingActor
import dapp.buildsomething.feature.splash.presentation.actor.LoadUserActor
import dapp.buildsomething.feature.splash.presentation.model.SplashState
import org.koin.dsl.module

val SplashModule = module {
    factory<SplashStoreProvider> {
        object : SplashStoreProvider() {
            override fun provide(): SplashStore {
                return SplashStore(
                    initialEvents = listOf(),
                    initialState = SplashState,
                    actors = setOf(
                        LoadUserActor(
                            userRepository = get(),
                            tokenProvider = get(),
                        ),
                        CheckOnboardingActor(
                            profileInteractor = get(),
                        ),
                    ),
                    reducer = SplashReducer(),
                )
            }
        }
    }
}
