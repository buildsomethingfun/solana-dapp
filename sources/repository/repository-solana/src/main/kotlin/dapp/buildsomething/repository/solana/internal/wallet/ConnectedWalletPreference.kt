package dapp.buildsomething.repository.solana.internal.wallet

import androidx.annotation.Keep
import dapp.buildsomething.repository.preferences.Preference
import dapp.buildsomething.repository.solana.ConnectedWallet

@Keep
internal data object ConnectedWalletPreference : Preference<ConnectedWallet> {
    override val key: String = "Key:ConnectedWallet"
    override val defaultValue = ConnectedWallet.Empty
    override val serializer = ConnectedWallet.serializer()
}
