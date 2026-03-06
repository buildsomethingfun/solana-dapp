package dapp.buildsomething.feature.apps.di

import dapp.buildsomething.feature.apps.details.presentation.AppDetailReducer
import dapp.buildsomething.feature.apps.details.presentation.AppDetailStore
import dapp.buildsomething.feature.apps.details.presentation.AppDetailStoreProvider
import dapp.buildsomething.feature.apps.details.presentation.AppDetailUiStateMapper
import dapp.buildsomething.feature.apps.details.presentation.actor.DeployAppActor
import dapp.buildsomething.feature.apps.details.presentation.actor.LoadAppDetailActor
import dapp.buildsomething.feature.apps.details.presentation.actor.PublishFlowActor
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailState
import dapp.buildsomething.feature.apps.list.presentation.AppsReducer
import dapp.buildsomething.feature.apps.list.presentation.AppsStore
import dapp.buildsomething.feature.apps.list.presentation.AppsStoreProvider
import dapp.buildsomething.feature.apps.list.presentation.AppsUiStateMapper
import dapp.buildsomething.feature.apps.list.presentation.actor.LoadAppsActor
import dapp.buildsomething.feature.apps.list.presentation.model.AppsState
import org.koin.dsl.module

val AppsModule = module {
    factory<AppsStoreProvider> {
        object : AppsStoreProvider() {
            override fun provide(): AppsStore {
                return AppsStore(
                    initialState = AppsState(),
                    actors = setOf(LoadAppsActor(appsInteractor = get())),
                    reducer = AppsReducer(),
                    uiStateMapper = AppsUiStateMapper(),
                )
            }
        }
    }
    factory<AppDetailStoreProvider> {
        object : AppDetailStoreProvider() {
            override fun provide(): AppDetailStore {
                return AppDetailStore(
                    initialState = AppDetailState(),
                    actors = setOf(
                        LoadAppDetailActor(appsInteractor = get()),
                        DeployAppActor(appsInteractor = get()),
                        PublishFlowActor(appsInteractor = get()),
                    ),
                    reducer = AppDetailReducer(),
                    uiStateMapper = AppDetailUiStateMapper(),
                )
            }
        }
    }
}
