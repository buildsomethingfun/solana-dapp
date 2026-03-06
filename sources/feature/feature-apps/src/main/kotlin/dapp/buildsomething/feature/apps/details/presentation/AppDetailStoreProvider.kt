package dapp.buildsomething.feature.apps.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailCommand
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEffect
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailState
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailUIEvent
import dapp.buildsomething.feature.apps.details.ui.AppDetailScreenState

abstract class AppDetailStoreProvider {

    internal abstract fun provide(): AppDetailStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class AppDetailStore(
    initialState: AppDetailState,
    actors: Set<Actor<AppDetailCommand, AppDetailEvent>>,
    reducer: Reducer<AppDetailCommand, AppDetailEffect, AppDetailEvent, AppDetailState>,
    uiStateMapper: AppDetailUiStateMapper,
) : TeaViewModel<AppDetailCommand, AppDetailEffect, AppDetailEvent, AppDetailUIEvent, AppDetailState, AppDetailScreenState>(
    initialState = initialState,
    actors = actors,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
)
