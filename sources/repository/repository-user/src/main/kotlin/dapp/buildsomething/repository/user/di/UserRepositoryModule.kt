package dapp.buildsomething.repository.user.di

import org.koin.dsl.module
import dapp.buildsomething.repository.user.UserRepository
import dapp.buildsomething.repository.user.internal.LocalUserRepository

val UserRepositoryModule = module {
    single<UserRepository> {
        LocalUserRepository(
            appPreferences = get()
        )
    }
}