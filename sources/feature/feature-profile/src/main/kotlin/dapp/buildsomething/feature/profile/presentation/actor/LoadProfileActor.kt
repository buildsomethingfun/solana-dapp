package dapp.buildsomething.feature.profile.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.repository.something.interactor.ProfileInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import dapp.buildsomething.feature.profile.presentation.model.ProfileCommand as Command
import dapp.buildsomething.feature.profile.presentation.model.ProfileEvent as Event

internal class LoadProfileActor(
    private val profileInteractor: ProfileInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.LoadProfile>()
            .mapLatest {
                profileInteractor.getPublisher()
                    .map { Event.ProfileLoaded(it) }
                    .getOrElse { Event.ProfileLoadFailed(it.message.orEmpty()) }
            }
    }
}
