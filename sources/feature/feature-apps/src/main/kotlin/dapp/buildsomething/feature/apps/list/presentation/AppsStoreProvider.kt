package dapp.buildsomething.feature.apps.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.apps.list.presentation.model.AppsCommand
import dapp.buildsomething.feature.apps.list.presentation.model.AppsEffect
import dapp.buildsomething.feature.apps.list.presentation.model.AppsEvent
import dapp.buildsomething.feature.apps.list.presentation.model.AppsState
import dapp.buildsomething.feature.apps.list.presentation.model.AppsUIEvent
import dapp.buildsomething.feature.apps.list.ui.AppsScreenState

abstract class AppsStoreProvider {

    internal abstract fun provide(): AppsStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class AppsStore(
    initialState: AppsState,
    actors: Set<Actor<AppsCommand, AppsEvent>>,
    reducer: Reducer<AppsCommand, AppsEffect, AppsEvent, AppsState>,
    initialEvents: List<AppsEvent> = emptyList(),
    uiStateMapper: AppsUiStateMapper,
) : TeaViewModel<AppsCommand, AppsEffect, AppsEvent, AppsUIEvent, AppsState, AppsScreenState>(
    initialState = initialState,
    actors = actors,
    reducer = reducer,
    initialEvents = initialEvents,
    uiStateMapper = uiStateMapper,
)
