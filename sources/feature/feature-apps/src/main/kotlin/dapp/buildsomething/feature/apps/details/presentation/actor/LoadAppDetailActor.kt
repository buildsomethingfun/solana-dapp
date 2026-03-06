package dapp.buildsomething.feature.apps.details.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.common.util.formatTimestamp
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent
import dapp.buildsomething.feature.apps.details.ui.AppDetailUiModel
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailCommand as Command
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent as Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class LoadAppDetailActor(
    private val appsInteractor: AppsInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.LoadAppDetail>()
            .mapLatest { command ->
                runCatching {
                    val detail = appsInteractor.getAppDetail(command.id).getOrThrow()
                    val publisherMint = appsInteractor.getPublisher().getOrNull()?.publisherMintAddress
                    AppDetailUiModel(
                        id = detail.id,
                        name = detail.name,
                        description = detail.description,
                        status = detail.status,
                        deployedUrl = detail.deployedUrl,
                        androidPackage = detail.androidPackage,
                        appMintAddress = detail.appMintAddress,
                        publisherMintAddress = publisherMint,
                        iconUrl = detail.iconUrl,
                        screenshots = detail.screenshots.orEmpty(),
                        licenseUrl = detail.licenseUrl,
                        privacyPolicyUrl = detail.privacyPolicyUrl,
                        copyrightUrl = detail.copyrightUrl,
                        createdAt = detail.createdAt.formatTimestamp,
                        updatedAt = detail.updatedAt.formatTimestamp,
                    )
                }
                    .mapCatching(AppDetailEvent::AppDetailLoaded)
                    .getOrElse { Event.AppDetailLoadFailed("Failed to load app details") }
            }
    }
}
