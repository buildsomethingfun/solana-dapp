package dapp.buildsomething.feature.profile.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.repository.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import dapp.buildsomething.feature.profile.presentation.model.ProfileCommand as Command
import dapp.buildsomething.feature.profile.presentation.model.ProfileEvent as Event

internal class SignOutActor(
    private val onboardingRepository: OnboardingRepository,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.SignOut>()
            .mapLatest {
                onboardingRepository.logOut()
                Event.LogOutCompleted
            }
    }
}
