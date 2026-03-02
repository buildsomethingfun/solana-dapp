package dapp.buildsomething.repository.something.internal.api.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
enum class AppStatus {
    @SerialName("draft") Draft,
    @SerialName("deployed") Deployed,
}

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
    val status: AppStatus,
    val deployedUrl: String? = null,
    val iconUrl: String? = null,
    val createdAt: Long,
)

@Serializable
data class AppDetailResponse(
    val id: String,
    val userId: String,
    val name: String,
    val description: String? = null,
    val status: AppStatus,
    val cfScriptName: String? = null,
    val cfD1Id: String? = null,
    val deployedUrl: String? = null,
    val cfDevScriptName: String? = null,
    val cfDevD1Id: String? = null,
    val devUrl: String? = null,
    val androidPackage: String? = null,
    val appMintAddress: String? = null,
    val iconUrl: String? = null,
    @Serializable(with = StringifiedListSerializer::class)
    val screenshots: List<String>? = null,
    val licenseUrl: String? = null,
    val privacyPolicyUrl: String? = null,
    val copyrightUrl: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val previewUrl: String? = null,
)

@Serializable
data class DeployResponse(
    val url: String,
)

@Serializable
data class LegalGenerateResponse(
    val licenseUrl: String,
    val privacyPolicyUrl: String,
    val copyrightUrl: String,
)

@Serializable
data class IconUploadResponse(
    val iconUrl: String,
)

@Serializable
data class ScreenshotGenerateResponse(
    val screenshots: List<String>,
)

internal object StringifiedListSerializer : KSerializer<List<String>?> {
    override val descriptor = PrimitiveSerialDescriptor("StringifiedList", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<String>? {
        val raw = decoder.decodeString()
        return runCatching {
            Json.decodeFromString(ListSerializer(String.serializer()), raw)
        }.getOrNull()
    }

    override fun serialize(encoder: Encoder, value: List<String>?) {
        encoder.encodeString(Json.encodeToString(ListSerializer(String.serializer()), value.orEmpty()))
    }
}
