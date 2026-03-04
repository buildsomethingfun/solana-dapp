package dapp.buildsomething.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dapp.buildsomething.common.arch.tea.TeaViewModel
import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.arch.tea.component.Reducer
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingCommand as Command
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEffect as Effect
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEvent as Event
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingState as State
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingUIEvent as UIEvent

abstract class OnboardingStoreProvider {

    internal abstract fun provide(): OnboardingStore

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return provide() as T
            }
        }
    }
}

internal class OnboardingStore(
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
