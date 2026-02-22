package dapp.buildsomething.feature.auth.presentation.actor

import dapp.buildsomething.common.arch.tea.component.Actor
import dapp.buildsomething.feature.auth.presentation.model.AuthCommand as Command
import dapp.buildsomething.feature.auth.presentation.model.AuthEvent as Event
import dapp.buildsomething.repository.solana.WalletRepository
import dapp.buildsomething.repository.user.UserRepository
import dapp.buildsomething.repository.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest

internal class ConnectWalletActor(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
) : Actor<Command, Event> {

    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.ConnectWallet>()
            .mapLatest {
                try {
                    walletRepository.connectWallet()
                    val wallet = walletRepository.getWallet()
                    userRepository.update {
                        User(id = wallet.publicKey, anonymous = false)
                    }
                    Event.AuthSuccess
                } catch (e: Exception) {
                    Event.AuthFailed(e.message ?: "Failed to connect wallet")
                }
            }
    }
}
