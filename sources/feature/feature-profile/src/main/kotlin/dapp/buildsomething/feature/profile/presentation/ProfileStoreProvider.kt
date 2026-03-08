package dapp.buildsomething.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.profile.presentation.model.ProfileCommand as Command
import dapp.buildsomething.feature.profile.presentation.model.ProfileEffect as Effect
import dapp.buildsomething.feature.profile.presentation.model.ProfileEvent as Event
import dapp.buildsomething.feature.profile.presentation.model.ProfileState as State
import dapp.buildsomething.feature.profile.presentation.model.ProfileUIEvent as UIEvent

abstract class ProfileStoreProvider {

    internal abstract fun provide(): ProfileStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class ProfileStore(
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
