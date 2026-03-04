package dapp.buildsomething.repository.onboarding.internal

import dapp.buildsomething.repository.onboarding.OnboardingRepository
import dapp.buildsomething.repository.preferences.AppPreferences
import dapp.buildsomething.repository.solana.WalletRepository
import dapp.buildsomething.repository.something.interactor.ProfileInteractor
import dapp.buildsomething.repository.something.internal.api.model.UpdatePublisherRequest
import dapp.buildsomething.repository.something.internal.jwt.JwtRepository
import dapp.buildsomething.repository.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class OnboardingRepositoryImpl(
    private val appPreferences: AppPreferences,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val jwtRepository: JwtRepository,
    private val profileInteractor: ProfileInteractor,
) : OnboardingRepository {

    override val isOnboarded: Flow<Boolean>
        get() = appPreferences.observe(OnboardedPreference).map { it ?: false }

    override suspend fun getName(): String {
        return userRepository.getUser().name
    }

    override suspend fun getEmail(): String {
        return userRepository.getUser().email
    }

    override suspend fun createPublisherProfile(name: String, email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                profileInteractor.updatePublisher(
                    UpdatePublisherRequest(displayName = name, contactEmail = email)
                ).getOrThrow()
                userRepository.update { copy(name = name, email = email) }
                appPreferences.setValue(OnboardedPreference, true)
            }
        }
    }

    override suspend fun logOut() {
        withContext(Dispatchers.IO) {
            jwtRepository.clear()
            walletRepository.disconnectWallet()
            userRepository.logOut()
        }
    }
}
