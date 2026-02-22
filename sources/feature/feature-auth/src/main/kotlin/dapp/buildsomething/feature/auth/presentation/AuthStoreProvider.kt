package dapp.buildsomething.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.auth.presentation.model.AuthCommand as Command
import dapp.buildsomething.feature.auth.presentation.model.AuthEffect as Effect
import dapp.buildsomething.feature.auth.presentation.model.AuthEvent as Event
import dapp.buildsomething.feature.auth.presentation.model.AuthState as State
import dapp.buildsomething.feature.auth.presentation.model.AuthUIEvent as UIEvent

abstract class AuthStoreProvider {

    internal abstract fun provide(): AuthStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class AuthStore(
    initialState: State,
    actors: Set<Actor<Command, Event>>,
    reducer: Reducer<Command, Effect, Event, State>,
    initialEvents: List<Event> = emptyList(),
) : TeaViewModel<Command, Effect, Event, UIEvent, State, State>(
    initialState = initialState,
    actors = actors,
    reducer = reducer,
    initialEvents = initialEvents,
)
