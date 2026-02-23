package dapp.buildsomething.feature.apps.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.apps.presentation.model.AppsCommand as Command
import dapp.buildsomething.feature.apps.presentation.model.AppsEvent as Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class LoadAppsActor : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.LoadApps>()
            .mapLatest {
                // TODO: Replace with actual API call
                Event.AppsLoadFailed("API is not available yet")
            }
    }
}
