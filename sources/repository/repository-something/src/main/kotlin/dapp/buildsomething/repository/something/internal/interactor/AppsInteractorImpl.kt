package dapp.buildsomething.repository.something.internal.interactor

import android.util.Base64
import dapp.buildsomething.repository.solana.WalletRepository
import dapp.buildsomething.repository.something.interactor.AppsInteractor
import dapp.buildsomething.repository.something.internal.api.SomethingApi
import dapp.buildsomething.repository.something.internal.api.model.AppDetailResponse
import dapp.buildsomething.repository.something.internal.api.model.AppResponse
import dapp.buildsomething.repository.something.internal.api.model.BuildConfirmRequest
import dapp.buildsomething.repository.something.internal.api.model.BuildStatusResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateAppNftRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateNftResponse
import dapp.buildsomething.repository.something.internal.api.model.CreatePublisherNftRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateReleaseNftRequest
import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateReleaseRequest
import dapp.buildsomething.repository.something.internal.api.model.IconUploadResponse
import dapp.buildsomething.repository.something.internal.api.model.LegalGenerateResponse
import dapp.buildsomething.repository.something.internal.api.model.PrepareAttestationResponse
import dapp.buildsomething.repository.something.internal.api.model.PublishSubmitRequest
import dapp.buildsomething.repository.something.internal.api.model.PublishSubmitResponse
import dapp.buildsomething.repository.something.internal.api.model.ReleaseResponse
import dapp.buildsomething.repository.something.internal.api.model.ScreenshotGenerateResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

internal class AppsInteractorImpl(
    private val api: SomethingApi,
    private val walletRepository: WalletRepository,
) : AppsInteractor {

    override suspend fun getListApps(): Result<List<AppResponse>> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getApps() }
        }
    }

    override suspend fun getAppDetail(id: String): Result<AppDetailResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getApp(id) }
        }
    }

    override suspend fun deployApp(id: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching { api.deployApp(id).url }
        }
    }

    override suspend fun generateLegal(id: String): Result<LegalGenerateResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.generateLegal(id) }
        }
    }

    override suspend fun uploadIcon(
        id: String,
        imageBytes: ByteArray,
        fileName: String,
    ): Result<IconUploadResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val requestBody = imageBytes.toRequestBody("image/*".toMediaType())
                val part = MultipartBody.Part.createFormData("icon", fileName, requestBody)
                api.uploadIcon(id, part)
            }
        }
    }

    override suspend fun getReleases(appId: String): Result<List<ReleaseResponse>> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getReleases(appId) }
        }
    }

    override suspend fun createRelease(id: String, version: String): Result<ReleaseResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.createRelease(id, CreateReleaseRequest(version)) }
        }
    }

    override suspend fun requestBuildPayment(
        appId: String,
        releaseId: String,
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching { api.requestBuildPayment(appId, releaseId).transaction }
        }
    }

    override suspend fun confirmBuild(
        appId: String,
        releaseId: String,
        signature: String,
    ): Result<ReleaseResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                api.confirmBuild(appId, releaseId, BuildConfirmRequest(signature))
                api.getReleases(appId).first { it.id == releaseId }
            }
        }
    }

    override suspend fun getBuildStatus(
        appId: String,
        releaseId: String,
    ): Result<BuildStatusResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getBuildStatus(appId, releaseId) }
        }
    }

    override suspend fun generateScreenshots(id: String): Result<ScreenshotGenerateResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.generateScreenshots(id) }
        }
    }

    override suspend fun generateIcon(id: String): Result<IconUploadResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.generateIcon(id) }
        }
    }

    override suspend fun signAndSendTransaction(base64Transaction: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching { walletRepository.signAndSendSerializedTransaction(base64Transaction) }
        }
    }

    override suspend fun getPublisher(): Result<PublisherResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getPublisher() }
        }
    }

    override suspend fun createPublisherNft(): Result<CreateNftResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val publisher = api.getPublisher()
                api.createPublisherNft(
                    CreatePublisherNftRequest(
                        publisherName = publisher.displayName ?: publisher.walletAddress,
                        publisherWebsite = "https://buildsomething.fun",
                        publisherEmail = publisher.contactEmail.orEmpty(),
                    )
                )
            }
        }
    }

    override suspend fun createAppNft(appId: String): Result<CreateNftResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val publisher = api.getPublisher()
                val publisherMint = publisher.publisherMintAddress
                    ?: error("Publisher mint address not found")
                api.createAppNft(CreateAppNftRequest(appId, publisherMint))
            }
        }
    }

    override suspend fun createReleaseNft(appId: String, releaseId: String): Result<CreateNftResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.createReleaseNft(CreateReleaseNftRequest(appId, releaseId)) }
        }
    }

    override suspend fun prepareAttestation(): Result<PrepareAttestationResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.prepareAttestation() }
        }
    }

    override suspend fun signAttestation(base64Buffer: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val bufferBytes = Base64.decode(base64Buffer, Base64.DEFAULT)
                val signatureBytes = walletRepository.signMessage(bufferBytes)
                Base64.encodeToString(signatureBytes, Base64.NO_WRAP)
            }
        }
    }

    override suspend fun submitPublish(
        appId: String,
        releaseId: String,
        signedAttestation: String,
        requestUniqueId: String,
        appMintAddress: String,
        releaseMintAddress: String,
    ): Result<PublishSubmitResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                api.submitPublish(
                    PublishSubmitRequest(
                        appId = appId,
                        releaseId = releaseId,
                        signedAttestation = signedAttestation,
                        requestUniqueId = requestUniqueId,
                        appMintAddress = appMintAddress,
                        releaseMintAddress = releaseMintAddress,
                    )
                )
            }
        }
    }
}
