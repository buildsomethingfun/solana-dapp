package dapp.buildsomething.feature.splash.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.splash.presentation.model.SplashEvent
import dapp.buildsomething.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import dapp.buildsomething.feature.splash.presentation.model.SplashCommand as Command
import dapp.buildsomething.feature.splash.presentation.model.SplashEvent as Event

internal class LoadUserActor(
    private val userRepository: UserRepository,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands.filterIsInstance<Command.LoadOnboardingState>()
            .flatMapLatest { userRepository.user.mapLatest { user -> !user.anonymous } }
            .mapLatest(SplashEvent::AuthStateLoaded)
    }
}