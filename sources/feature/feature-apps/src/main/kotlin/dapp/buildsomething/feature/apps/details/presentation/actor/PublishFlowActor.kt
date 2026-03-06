package dapp.buildsomething.feature.apps.details.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailCommand as Command
import dapp.buildsomething.feature.apps.details.presentation.model.AppDetailEvent as Event
import dapp.buildsomething.feature.apps.details.presentation.model.PublishingStep
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.repository.something.internal.api.model.ReleaseStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

internal class PublishFlowActor(
    private val appsInteractor: AppsInteractor,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands.filterIsInstance<Command.PublishApp>()
            .flatMapLatest { cmd ->
                flow {
                    val appId = cmd.appId

                    // Step 1: Resolve existing state to determine resume point
                    val releases = appsInteractor.getReleases(appId).getOrDefault(emptyList())
                    val latestRelease = releases.maxByOrNull { it.createdAt ?: 0L }
                    val detail = appsInteractor.getAppDetail(appId).getOrNull()
                    val hasLegal = detail?.licenseUrl != null
                    val hasScreenshots = !detail?.screenshots.isNullOrEmpty()
                    val hasIcon = detail?.iconUrl != null

                    var releaseId: String

                    when {
                        // Build already done → skip to post-build
                        latestRelease != null && latestRelease.status == ReleaseStatus.ReadyToPublish -> {
                            releaseId = latestRelease.id
                        }
                        // Still building → poll until done
                        latestRelease != null && latestRelease.status == ReleaseStatus.Building -> {
                            releaseId = latestRelease.id
                            emit(Event.PublishStepUpdate(PublishingStep.Building))
                            pollUntilBuilt(appId, releaseId)
                        }
                        // Draft or failed release → start from payment
                        latestRelease != null && (latestRelease.status == ReleaseStatus.Draft || latestRelease.status == ReleaseStatus.BuildFailed) -> {
                            releaseId = latestRelease.id
                            emit(Event.PublishStepUpdate(PublishingStep.RequestingPayment))
                            val tx = appsInteractor.requestBuildPayment(appId, releaseId).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.SigningPayment))
                            val signature = appsInteractor.signAndSendTransaction(tx).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.ConfirmingBuild))
                            appsInteractor.confirmBuild(appId, releaseId, signature).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.Building))
                            pollUntilBuilt(appId, releaseId)
                        }
                        // No release → full pipeline
                        else -> {
                            emit(Event.PublishStepUpdate(PublishingStep.CreatingRelease))
                            val release = appsInteractor.createRelease(appId, "1.0.0").getOrThrow()
                            releaseId = release.id
                            emit(Event.PublishStepUpdate(PublishingStep.RequestingPayment))
                            val tx = appsInteractor.requestBuildPayment(appId, releaseId).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.SigningPayment))
                            val signature = appsInteractor.signAndSendTransaction(tx).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.ConfirmingBuild))
                            appsInteractor.confirmBuild(appId, releaseId, signature).getOrThrow()
                            emit(Event.PublishStepUpdate(PublishingStep.Building))
                            pollUntilBuilt(appId, releaseId)
                        }
                    }

                    // Post-build phase
                    if (!hasLegal) {
                        emit(Event.PublishStepUpdate(PublishingStep.GeneratingLegal))
                        appsInteractor.generateLegal(appId).getOrThrow()
                    }

                    if (!hasScreenshots) {
                        emit(Event.PublishStepUpdate(PublishingStep.GeneratingScreenshots))
                        appsInteractor.generateScreenshots(appId).getOrThrow()
                    }

                    if (!hasIcon) {
                        emit(Event.PublishStepUpdate(PublishingStep.GeneratingIcon))
                        appsInteractor.generateIcon(appId).getOrThrow()
                    }

                    // Ensure publisher NFT exists
                    val publisher = appsInteractor.getPublisher().getOrThrow()
                    if (publisher.publisherMintAddress == null) {
                        emit(Event.PublishStepUpdate(PublishingStep.CreatingPublisherNft))
                        val publisherNft = appsInteractor.createPublisherNft().getOrThrow()
                        emit(Event.PublishStepUpdate(PublishingStep.SigningPublisherNft))
                        appsInteractor.signAndSendTransaction(publisherNft.transaction).getOrThrow()
                    }

                    emit(Event.PublishStepUpdate(PublishingStep.CreatingAppNft))
                    val appNft = appsInteractor.createAppNft(appId).getOrThrow()
                    emit(Event.PublishStepUpdate(PublishingStep.SigningAppNft))
                    appsInteractor.signAndSendTransaction(appNft.transaction).getOrThrow()
                    val appMintAddress = appNft.mintAddress

                    emit(Event.PublishStepUpdate(PublishingStep.CreatingReleaseNft))
                    val releaseNft = appsInteractor.createReleaseNft(appId, releaseId).getOrThrow()
                    emit(Event.PublishStepUpdate(PublishingStep.SigningReleaseNft))
                    appsInteractor.signAndSendTransaction(releaseNft.transaction).getOrThrow()
                    val releaseMintAddress = releaseNft.mintAddress

                    emit(Event.PublishStepUpdate(PublishingStep.PreparingAttestation))
                    val attestation = appsInteractor.prepareAttestation().getOrThrow()
                    emit(Event.PublishStepUpdate(PublishingStep.SigningAttestation))
                    val signedAttestation = appsInteractor.signAttestation(attestation.attestationBuffer).getOrThrow()

                    emit(Event.PublishStepUpdate(PublishingStep.Publishing))
                    appsInteractor.submitPublish(
                        appId = appId,
                        releaseId = releaseId,
                        signedAttestation = signedAttestation,
                        requestUniqueId = attestation.requestUniqueId,
                        appMintAddress = appMintAddress,
                        releaseMintAddress = releaseMintAddress,
                    ).getOrThrow()

                    emit(Event.PublishCompleted)
                }.catchAndEmitFailure()
            }
    }

    private suspend fun pollUntilBuilt(appId: String, releaseId: String) {
        while (true) {
            delay(5_000)
            val status = appsInteractor.getBuildStatus(appId, releaseId).getOrThrow()
            when (status.status) {
                ReleaseStatus.ReadyToPublish -> break
                ReleaseStatus.BuildFailed -> error("Build failed")
                else -> continue
            }
        }
    }

    private fun Flow<Event>.catchAndEmitFailure(): Flow<Event> = flow {
        try {
            collect { emit(it) }
        } catch (e: Throwable) {
            emit(Event.PublishFailed(e.message ?: "Publishing failed"))
        }
    }
}
