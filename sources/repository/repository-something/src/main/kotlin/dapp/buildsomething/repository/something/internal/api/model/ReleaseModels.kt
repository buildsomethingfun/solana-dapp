package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.Serializable

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
    val status: String,
    val apkUrl: String? = null,
    val releaseMintAddress: String? = null,
    val createdAt: Long? = null,
)
