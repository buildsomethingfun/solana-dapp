package dapp.buildsomething.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.splash.presentation.model.SplashCommand as Command
import dapp.buildsomething.feature.splash.presentation.model.SplashEffect as Effect
import dapp.buildsomething.feature.splash.presentation.model.SplashEvent as Event
import dapp.buildsomething.feature.splash.presentation.model.SplashState as State
import dapp.buildsomething.feature.splash.presentation.model.SplashUIEvent as UIEvent

abstract class SplashStoreProvider {

    internal abstract fun provide(): SplashStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class SplashStore(
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
