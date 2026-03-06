package dapp.buildsomething.feature.apps.list.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.apps.list.presentation.model.AppsEvent
import dapp.buildsomething.feature.apps.list.ui.AppUiModel
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.feature.apps.list.presentation.model.AppsCommand as Command
import dapp.buildsomething.feature.apps.list.presentation.model.AppsEvent as Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class LoadAppsActor(
    private val appsInteractor: AppsInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.LoadApps>()
            .mapLatest {
                appsInteractor.getListApps()
                    .mapCatching { apps ->
                        apps.map { app ->
                            AppUiModel(
                                id = app.id,
                                name = app.name,
                                description = app.status.name,
                                imageUrl = app.iconUrl,
                            )
                        }
                    }
                    .mapCatching(AppsEvent::AppsLoaded)
                    .getOrElse { Event.AppsLoadFailed("API is not available yet") }
            }
    }
}
