package dapp.buildsomething.repository.something.interactor

import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherRequest

interface ProfileInteractor {

    suspend fun getPublisher(): Result<PublisherResponse>

    suspend fun updatePublisher(request: UpdatePublisherRequest): Result<Boolean>
}
