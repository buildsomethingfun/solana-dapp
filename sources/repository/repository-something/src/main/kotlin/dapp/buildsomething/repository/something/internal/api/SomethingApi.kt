package dapp.buildsomething.repository.something.internal.api

import dapp.buildsomething.repository.something.internal.api.model.AppDetailResponse
import dapp.buildsomething.repository.something.internal.api.model.AppResponse
import dapp.buildsomething.repository.something.internal.api.model.AuthChallengeResponse
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyRequest
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyResponse
import dapp.buildsomething.repository.something.internal.api.model.ChatMessage
import dapp.buildsomething.repository.something.internal.api.model.ChatRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateAppRequest
import dapp.buildsomething.repository.something.internal.api.model.CreateAppResponse
import dapp.buildsomething.repository.something.internal.api.model.CreateReleaseRequest
import dapp.buildsomething.repository.something.internal.api.model.DeployResponse
import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenRequest
import dapp.buildsomething.repository.something.internal.api.model.RefreshTokenResponse
import dapp.buildsomething.repository.something.internal.api.model.ReleaseResponse
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherRequest
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/apps/{id}/releases")
    suspend fun getReleases(@Path("id") appId: String): List<ReleaseResponse>

    @POST("api/apps/{id}/releases")
    suspend fun createRelease(
        @Path("id") appId: String,
        @Body request: CreateReleaseRequest,
    ): ReleaseResponse

    @GET("api/publisher")
    suspend fun getPublisher(): PublisherResponse

    @PUT("api/publisher")
    suspend fun updatePublisher(@Body request: UpdatePublisherRequest): UpdatePublisherResponse
}
