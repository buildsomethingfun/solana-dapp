package dapp.buildsomething.feature.splash.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.repository.something.interactor.ProfileInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import dapp.buildsomething.feature.splash.presentation.model.SplashCommand as Command
import dapp.buildsomething.feature.splash.presentation.model.SplashEvent as Event

internal class CheckOnboardingActor(
    private val profileInteractor: ProfileInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.CheckOnboardingStatus>()
            .mapLatest {
                val needsOnboarding = profileInteractor.getPublisher()
                    .map { it.displayName.isNullOrBlank() || it.contactEmail.isNullOrBlank() }
                    .getOrDefault(true)
                Event.OnboardingStatusLoaded(needsOnboarding)
            }
    }
}
