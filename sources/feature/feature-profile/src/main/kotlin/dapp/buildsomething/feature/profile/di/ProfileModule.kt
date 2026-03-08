package dapp.buildsomething.feature.profile.di

import dapp.buildsomething.feature.profile.presentation.ProfileReducer
import dapp.buildsomething.feature.profile.presentation.ProfileStore
import dapp.buildsomething.feature.profile.presentation.ProfileStoreProvider
import dapp.buildsomething.feature.profile.presentation.actor.LoadProfileActor
import dapp.buildsomething.feature.profile.presentation.actor.SignOutActor
import dapp.buildsomething.feature.profile.presentation.model.ProfileState
import dapp.buildsomething.feature.profile.presentation.model.ProfileUIEvent
import org.koin.dsl.module

val ProfileModule = module {
    factory<ProfileStoreProvider> {
        object : ProfileStoreProvider() {
            override fun provide(): ProfileStore {
                return ProfileStore(
                    initialState = ProfileState(),
                    initialEvents = listOf(ProfileUIEvent.LoadProfile),
                    actors = setOf(
                        LoadProfileActor(
                            profileInteractor = get(),
                        ),
                        SignOutActor(
                            onboardingRepository = get(),
                        ),
                    ),
                    reducer = ProfileReducer(),
                )
            }
        }
    }
}
