package dapp.buildsomething.feature.apps.app.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.apps.app.presentation.model.AppsEvent
import dapp.buildsomething.feature.apps.app.ui.AppUiModel
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.feature.apps.app.presentation.model.AppsCommand as Command
import dapp.buildsomething.feature.apps.app.presentation.model.AppsEvent as Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

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
                                description = app.status, // TODO
                                imageUrl = app.deployedUrl, // TODO
                            )
                        }
                    }
                    .mapCatching(AppsEvent::AppsLoaded)
                    .getOrElse { Event.AppsLoadFailed("API is not available yet") }
            }
    }
}
