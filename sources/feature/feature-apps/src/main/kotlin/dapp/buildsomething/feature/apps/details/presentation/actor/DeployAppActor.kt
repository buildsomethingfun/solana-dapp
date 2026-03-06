package dapp.buildsomething.feature.apps.details.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailCommand as Command
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent as Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class DeployAppActor(
    private val appsInteractor: AppsInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.DeployApp>()
            .mapLatest { command ->
                appsInteractor.deployApp(command.id)
                    .map { url -> Event.DeploySuccess(url) }
                    .getOrElse { Event.DeployFailed("Failed to deploy app") }
            }
    }
}
