package dapp.buildsomething.repository.something.interactor

import dapp.buildsomething.repository.something.internal.api.model.AppDetailResponse
import dapp.buildsomething.repository.something.internal.api.model.AppResponse
import dapp.buildsomething.repository.something.internal.api.model.BuildStatusResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateNftResponse
import dapp.buildsomething.repository.something.internal.api.model.IconUploadResponse
import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.LegalGenerateResponse
import dapp.buildsomething.repository.something.internal.api.model.PrepareAttestationResponse
import dapp.buildsomething.repository.something.internal.api.model.PublishSubmitResponse
import dapp.buildsomething.repository.something.internal.api.model.ReleaseResponse
import dapp.buildsomething.repository.something.internal.api.model.ScreenshotGenerateResponse

interface AppsInteractor {

    suspend fun getListApps(): Result<List<AppResponse>>

    suspend fun getAppDetail(id: String): Result<AppDetailResponse>

    suspend fun deployApp(id: String): Result<String>

    suspend fun generateLegal(id: String): Result<LegalGenerateResponse>

    suspend fun uploadIcon(id: String, imageBytes: ByteArray, fileName: String): Result<IconUploadResponse>

    suspend fun getReleases(appId: String): Result<List<ReleaseResponse>>

    suspend fun createRelease(id: String, version: String): Result<ReleaseResponse>

    suspend fun requestBuildPayment(appId: String, releaseId: String): Result<String>

    suspend fun confirmBuild(appId: String, releaseId: String, signature: String): Result<ReleaseResponse>

    suspend fun getBuildStatus(appId: String, releaseId: String): Result<BuildStatusResponse>

    suspend fun generateScreenshots(id: String): Result<ScreenshotGenerateResponse>

    suspend fun generateIcon(id: String): Result<IconUploadResponse>

    suspend fun signAndSendTransaction(base64Transaction: String): Result<String>

    suspend fun getPublisher(): Result<PublisherResponse>

    suspend fun createPublisherNft(): Result<CreateNftResponse>

    suspend fun createAppNft(appId: String): Result<CreateNftResponse>

    suspend fun createReleaseNft(appId: String, releaseId: String): Result<CreateNftResponse>

    suspend fun prepareAttestation(): Result<PrepareAttestationResponse>

    suspend fun signAttestation(base64Buffer: String): Result<String>

    suspend fun submitPublish(
        appId: String,
        releaseId: String,
        signedAttestation: String,
        requestUniqueId: String,
        appMintAddress: String,
        releaseMintAddress: String,
    ): Result<PublishSubmitResponse>
}
