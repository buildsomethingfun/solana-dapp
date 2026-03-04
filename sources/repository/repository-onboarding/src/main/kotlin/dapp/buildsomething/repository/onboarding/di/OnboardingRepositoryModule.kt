package dapp.buildsomething.repository.onboarding.di

import dapp.buildsomething.repository.onboarding.OnboardingRepository
import dapp.buildsomething.repository.onboarding.internal.OnboardingRepositoryImpl
import org.koin.dsl.module

val OnboardingRepositoryModule = module {
    single<OnboardingRepository> {
        OnboardingRepositoryImpl(
            appPreferences = get(),
            userRepository = get(),
            walletRepository = get(),
            jwtRepository = get(),
            profileInteractor = get(),
        )
    }
}
