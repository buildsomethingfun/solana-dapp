package dapp.buildsomething.repository.something.interactor

import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyResponse

interface AuthInteractor {

    suspend fun auth(): Result<AuthVerifyResponse>
}
