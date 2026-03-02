package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReleaseStatus {
    @SerialName("draft") Draft,
    @SerialName("building") Building,
    @SerialName("build_failed") BuildFailed,
    @SerialName("ready_to_publish") ReadyToPublish,
    @SerialName("publishing") Publishing,
    @SerialName("published") Published,
    @SerialName("publish_failed") PublishFailed,
}

@Serializable
data class CreateReleaseRequest(
    val version: String,
)

@Serializable
data class ReleaseResponse(
    val id: String,
    val appId: String? = null,
    val version: String,
    val versionCode: Int,
    val status: ReleaseStatus,
    val apkUrl: String? = null,
    val releaseMintAddress: String? = null,
    val createdAt: Long? = null,
)

@Serializable
data class BuildResponse(
    val buildId: String,
    val status: ReleaseStatus,
)

@Serializable
data class BuildStatusResponse(
    val status: ReleaseStatus,
    val apkUrl: String? = null,
    val error: String? = null,
)

@Serializable
data class BuildPaymentResponse(
    val transaction: String,
)

@Serializable
data class BuildConfirmRequest(
    val signature: String,
)

@Serializable
data class CreateAppNftRequest(
    val appId: String,
    val publisherMintAddress: String,
)

@Serializable
data class CreateReleaseNftRequest(
    val appId: String,
    val releaseId: String,
)

@Serializable
data class CreateNftResponse(
    val transaction: String,
    val mintAddress: String,
)

@Serializable
data class PrepareAttestationResponse(
    val attestationBuffer: String,
    val requestUniqueId: String,
)

@Serializable
data class PublishSubmitRequest(
    val appId: String,
    val releaseId: String,
    val signedAttestation: String,
    val requestUniqueId: String,
    val appMintAddress: String,
    val releaseMintAddress: String,
)

@Serializable
data class PublishSubmitResponse(
    val success: Boolean,
)
