package dapp.buildsomething.feature.onboarding.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.repository.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingCommand as Command
import dapp.buildsomething.feature.onboarding.presentation.model.OnboardingEvent as Event

internal class CreateProfileActor(
    private val onboardingRepository: OnboardingRepository,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.CreateProfile>()
            .mapLatest { command ->
                onboardingRepository.createPublisherProfile(command.name, command.email)
                    .map { Event.ProfileCreated }
                    .getOrElse { Event.ProfileCreationFailed(it.message.orEmpty()) }
            }
    }
}
