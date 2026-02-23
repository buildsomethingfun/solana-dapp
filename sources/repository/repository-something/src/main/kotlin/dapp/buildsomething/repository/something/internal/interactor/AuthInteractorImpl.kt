package dapp.buildsomething.repository.something.internal.interactor

import android.util.Base64
import com.solana.mobilewalletadapter.common.signin.SignInWithSolana
import dapp.buildsomething.repository.solana.WalletRepository
import dapp.buildsomething.repository.solana.internal.core.Base58
import dapp.buildsomething.repository.something.interactor.AuthInteractor
import dapp.buildsomething.repository.something.internal.api.SomethingApi
import dapp.buildsomething.repository.something.internal.api.SomethingAuthApi
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyInput
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyOutput
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyOutputAccount
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyRequest
import dapp.buildsomething.repository.something.internal.api.model.AuthVerifyResponse
import dapp.buildsomething.repository.something.internal.jwt.JwtRepository
import dapp.buildsomething.repository.something.internal.jwt.model.JwtToken
import dapp.buildsomething.repository.user.UserRepository
import dapp.buildsomething.repository.user.model.User
import timber.log.Timber

internal class AuthInteractorImpl(
    private val api: SomethingAuthApi,
    private val walletRepository: WalletRepository,
    private val jwtRepository: JwtRepository,
    private val userRepository: UserRepository,
) : AuthInteractor {

    override suspend fun auth(): Result<AuthVerifyResponse> = runCatching {
        walletRepository.connectWallet()
        val wallet = walletRepository.getWallet()
        val challenge = api.getAuthChallenge()
        val signInPayload = SignInWithSolana.Payload(
            /*domain*/ challenge.domain,
            /*address*/ wallet.publicKey,
            /*statement*/ challenge.statement,
            /*uri*/ null,
            /*version*/ null,
            /*chainId*/ challenge.chainId,
            /*nonce*/ challenge.nonce,
            /*issuedAt*/ challenge.issuedAt,
            /*expirationTime*/ null,
            /*notBefore*/ null,
            /*requestId*/ null,
            /*resources*/ null,
        )
        val messageBytes = signInPayload.prepareMessage().toByteArray()
        val signature = walletRepository.signMessage(messageBytes)

        api.verifyAuth(
            AuthVerifyRequest(
                input = AuthVerifyInput(
                    domain = challenge.domain,
                    statement = challenge.statement,
                    nonce = challenge.nonce,
                    issuedAt = challenge.issuedAt,
                    chainId = challenge.chainId,
                ),
                output = AuthVerifyOutput(
                    account = AuthVerifyOutputAccount(
                        publicKey = Base64.encodeToString(
                            Base58.decode(wallet.publicKey),
                            Base64.NO_WRAP,
                        ),
                        address = wallet.publicKey,
                    ),
                    signedMessage = Base64.encodeToString(messageBytes, Base64.NO_WRAP),
                    signature = Base64.encodeToString(signature, Base64.NO_WRAP),
                ),
            )
        )
    }.onSuccess { response ->
        jwtRepository.setToken(JwtToken(response.token, response.refreshToken))
        userRepository.update {
            User(id = response.user.id, anonymous = false)
        }
    }
}
