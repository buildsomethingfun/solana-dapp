package dapp.buildsomething.repository.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {

    val isOnboarded: Flow<Boolean>

    suspend fun getName(): String

    suspend fun getEmail(): String

    suspend fun createPublisherProfile(name: String, email: String): Result<Unit>

    suspend fun logOut()
}
