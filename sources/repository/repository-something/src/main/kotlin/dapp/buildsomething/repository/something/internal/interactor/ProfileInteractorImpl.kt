package dapp.buildsomething.repository.something.internal.interactor

import dapp.buildsomething.repository.something.interactor.ProfileInteractor
import dapp.buildsomething.repository.something.internal.api.SomethingApi
import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ProfileInteractorImpl(
    private val api: SomethingApi,
) : ProfileInteractor {

    override suspend fun getPublisher(): Result<PublisherResponse> {
        return withContext(Dispatchers.IO) {
            runCatching { api.getPublisher() }
        }
    }

    override suspend fun updatePublisher(request: UpdatePublisherRequest): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            runCatching { api.updatePublisher(request).ok }
        }
    }
}
