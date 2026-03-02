package dapp.buildsomething.repository.something.internal.api

import dapp.buildsomething.repository.something.internal.api.model.AppDetailResponse
import dapp.buildsomething.repository.something.internal.api.model.AppResponse
import dapp.buildsomething.repository.something.internal.api.model.AuthChallengeResponse
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyRequest
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyResponse
import dapp.buildsomething.repository.something.internal.api.model.BuildConfirmRequest
import dapp.buildsomething.repository.something.internal.api.model.BuildPaymentResponse
import dapp.buildsomething.repository.something.internal.api.model.BuildResponse
import dapp.buildsomething.repository.something.internal.api.model.BuildStatusResponse
import dapp.buildsomething.repository.something.internal.api.model.ChatMessage
import dapp.buildsomething.repository.something.internal.api.model.ChatRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateAppRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateAppResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateReleaseRequest
import dapp.buildsomething.repository.something.internal.api.model.DeployResponse
import dapp.buildsomething.repository.something.internal.api.model.IconUploadResponse
import dapp.buildsomething.repository.something.internal.api.model.LegalGenerateResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateAppNftRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateNftResponse
import dapp.buildsomething.repository.something.internal.api.model.CreatePublisherNftRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateReleaseNftRequest
import dapp.buildsomething.repository.something.internal.api.model.PrepareAttestationResponse
import dapp.buildsomething.repository.something.internal.api.model.PublishSubmitRequest
import dapp.buildsomething.repository.something.internal.api.model.PublishSubmitResponse
import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenRequest
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenResponse
import dapp.buildsomething.repository.something.internal.api.model.ReleaseResponse
import dapp.buildsomething.repository.something.internal.api.model.ScreenshotGenerateResponse
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherRequest
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

internal interface SomethingApi {

    @Streaming
    @POST("api/chat")
    suspend fun chat(@Body request: ChatRequest): ResponseBody

    @GET("api/chat")
    suspend fun getChatHistory(@Query("appId") appId: String): List<ChatMessage>

    @Streaming
    @GET("api/chat/{appId}/stream")
    suspend fun resumeChatStream(@Path("appId") appId: String): ResponseBody

    @GET("api/apps")
    suspend fun getApps(): List<AppResponse>

    @POST("api/apps")
    suspend fun createApp(@Body request: CreateAppRequest): CreateAppResponse

    @GET("api/apps/{id}")
    suspend fun getApp(@Path("id") id: String): AppDetailResponse

    @POST("api/apps/{id}/deploy")
    suspend fun deployApp(@Path("id") id: String): DeployResponse

    @POST("api/apps/{id}/preview")
    suspend fun previewApp(@Path("id") id: String): DeployResponse

    @POST("api/apps/{id}/legal/generate")
    suspend fun generateLegal(@Path("id") id: String): LegalGenerateResponse

    @POST("api/apps/{id}/icon/generate")
    suspend fun generateIcon(@Path("id") id: String): IconUploadResponse

    @Multipart
    @PUT("api/apps/{id}/icon")
    suspend fun uploadIcon(
        @Path("id") id: String,
        @Part icon: MultipartBody.Part,
    ): IconUploadResponse

    @GET("api/apps/{id}/releases")
    suspend fun getReleases(@Path("id") appId: String): List<ReleaseResponse>

    @POST("api/apps/{id}/releases")
    suspend fun createRelease(
        @Path("id") appId: String,
        @Body request: CreateReleaseRequest,
    ): ReleaseResponse

    @POST("api/apps/{id}/releases/{releaseId}/build")
    suspend fun requestBuildPayment(
        @Path("id") appId: String,
        @Path("releaseId") releaseId: String,
    ): BuildPaymentResponse

    @POST("api/apps/{id}/releases/{releaseId}/build/confirm")
    suspend fun confirmBuild(
        @Path("id") appId: String,
        @Path("releaseId") releaseId: String,
        @Body request: BuildConfirmRequest,
    ): BuildResponse

    @GET("api/apps/{id}/releases/{releaseId}/build/status")
    suspend fun getBuildStatus(
        @Path("id") appId: String,
        @Path("releaseId") releaseId: String,
    ): BuildStatusResponse

    @POST("api/apps/{id}/screenshots/generate")
    suspend fun generateScreenshots(@Path("id") id: String): ScreenshotGenerateResponse

    @POST("api/publish/create-publisher-nft")
    suspend fun createPublisherNft(@Body request: CreatePublisherNftRequest): CreateNftResponse

    @POST("api/publish/create-app-nft")
    suspend fun createAppNft(@Body request: CreateAppNftRequest): CreateNftResponse

    @POST("api/publish/create-release-nft")
    suspend fun createReleaseNft(@Body request: CreateReleaseNftRequest): CreateNftResponse

    @POST("api/publish/prepare-attestation")
    suspend fun prepareAttestation(): PrepareAttestationResponse

    @POST("api/publish/submit")
    suspend fun submitPublish(@Body request: PublishSubmitRequest): PublishSubmitResponse

    @GET("api/publisher")
    suspend fun getPublisher(): PublisherResponse

    @PUT("api/publisher")
    suspend fun updatePublisher(@Body request: UpdatePublisherRequest): UpdatePublisherResponse
}
