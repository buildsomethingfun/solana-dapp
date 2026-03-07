package dapp.buildsomething.feature.newapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.newapp.presentation.model.NewAppCommand
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEffect
import dapp.buildsomething.feature.newapp.presentation.model.NewAppEvent
import dapp.buildsomething.feature.newapp.presentation.model.NewAppState
import dapp.buildsomething.feature.newapp.presentation.model.NewAppUIEvent
import dapp.buildsomething.feature.newapp.ui.model.NewAppUiState

abstract class NewAppStoreProvider {
    internal abstract fun provide(): NewAppStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class NewAppStore(
    initialState: NewAppState,
    actors: Set<Actor<NewAppCommand, NewAppEvent>>,
    reducer: Reducer<NewAppCommand, NewAppEffect, NewAppEvent, NewAppState>,
    uiStateMapper: NewAppUiStateMapper,
) : TeaViewModel<NewAppCommand, NewAppEffect, NewAppEvent, NewAppUIEvent, NewAppState, NewAppUiState>(
    initialState = initialState,
    actors = actors,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
)
