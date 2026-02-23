package dapp.buildsomething.repository.something.interactor

import dapp.buildsomething.repository.something.interactor.model.ChatStream
import dapp.buildsomething.repository.something.internal.api.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatInteractor {

    suspend fun sendMessage(appId: String, text: String): Flow<ChatStream>

    suspend fun getChatHistory(appId: String): List<ChatMessage>

    suspend fun resumeStream(appId: String): Flow<ChatStream>
}
