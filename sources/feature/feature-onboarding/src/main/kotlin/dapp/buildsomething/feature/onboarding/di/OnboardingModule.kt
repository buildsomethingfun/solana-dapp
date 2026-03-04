package dapp.buildsomething.feature.onboarding.di

import dapp.buildsomething.feature.onboarding.presentation.OnboardingReducer
import dapp.buildsomething.feature.onboarding.presentation.OnboardingStore
import dapp.buildsomething.feature.onboarding.presentation.OnboardingStoreProvider
import dapp.buildsomething.feature.onboarding.presentation.actor.CreateProfileActor
import dapp.buildsomething.feature.onboarding.presentation.actor.LogOutActor
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingState
import org.koin.dsl.module

val OnboardingModule = module {
    factory<OnboardingStoreProvider> {
        object : OnboardingStoreProvider() {
            override fun provide(): OnboardingStore {
                return OnboardingStore(
                    initialEvents = listOf(),
                    initialState = OnboardingState(),
                    actors = setOf(
                        LogOutActor(
                            onboardingRepository = get(),
                        ),
                        CreateProfileActor(
                            onboardingRepository = get(),
                        ),
                    ),
                    reducer = OnboardingReducer(),
                )
            }
        }
    }
}
