package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePublisherRequest(
    @SerialName("display_name")
    val displayName: String? = null,
    val website: String? = null,
    @SerialName("contact_email")
    val contactEmail: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
)

@Serializable
data class UpdatePublisherResponse(
    val ok: Boolean,
)

@Serializable
data class CreatePublisherNftRequest(
    val publisherName: String,
    val publisherWebsite: String,
    val publisherEmail: String,
)

@Serializable
data class PublisherResponse(
    val id: String,
    val walletAddress: String,
    val displayName: String? = null,
    val website: String? = null,
    val contactEmail: String? = null,
    val avatarUrl: String? = null,
    val publisherMintAddress: String? = null,
)
