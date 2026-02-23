package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateAppRequest(
    val name: String,
)

@Serializable
data class CreateAppResponse(
    val id: String,
)

@Serializable
data class AppResponse(
    val id: String,
    val name: String,
    val status: String,
    val deployedUrl: String? = null,
    val createdAt: Long,
)

@Serializable
data class AppDetailResponse(
    val id: String,
    val userId: String,
    val name: String,
    val status: String,
    val deployedUrl: String? = null,
    val files: Map<String, String> = emptyMap(),
    val androidPackage: String? = null,
    val createdAt: Long,
)

@Serializable
data class DeployResponse(
    val url: String,
)
