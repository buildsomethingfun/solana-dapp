package dapp.buildsomething.feature.apps.di

import dapp.buildsomething.feature.apps.presentation.AppsReducer
import dapp.buildsomething.feature.apps.presentation.AppsStore
import dapp.buildsomething.feature.apps.presentation.AppsStoreProvider
import dapp.buildsomething.feature.apps.presentation.AppsUiStateMapper
import dapp.buildsomething.feature.apps.presentation.actor.LoadAppsActor
import dapp.buildsomething.feature.apps.presentation.model.AppsState
import dapp.buildsomething.feature.apps.presentation.model.AppsUIEvent
import org.koin.dsl.module

val AppsModule = module {
    factory<AppsStoreProvider> {
        object : AppsStoreProvider() {
            override fun provide(): AppsStore {
                return AppsStore(
                    initialState = AppsState(),
                    actors = setOf(LoadAppsActor()),
                    reducer = AppsReducer(),
                    initialEvents = listOf(AppsUIEvent.LoadApps),
                    uiStateMapper = AppsUiStateMapper(),
                )
            }
        }
    }
}
