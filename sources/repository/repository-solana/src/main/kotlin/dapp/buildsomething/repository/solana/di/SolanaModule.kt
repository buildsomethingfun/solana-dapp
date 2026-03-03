package dapp.buildsomething.repository.solana.di

import androidx.core.net.toUri
import dapp.buildsomething.repository.solana.BuildConfig
import com.solana.mobilewalletadapter.clientlib.ConnectionIdentity
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.Solana
import org.koin.dsl.module
import dapp.buildsomething.repository.solana.WalletRepository
import dapp.buildsomething.repository.solana.activity.ActivityResultSenderProvider
import dapp.buildsomething.repository.solana.internal.WalletRepositoryImpl
import dapp.buildsomething.repository.solana.internal.core.Connection
import dapp.buildsomething.repository.solana.internal.core.RpcUrl
import dapp.buildsomething.repository.solana.internal.wallet.MobileWalletAdapterWrapper

val SolanaModule = module {

    single<ActivityResultSenderProvider>(
        createdAtStart = true,
    ) {
        ActivityResultSenderProvider(get())
    }

    single<MobileWalletAdapter> {
        MobileWalletAdapter(
            connectionIdentity = ConnectionIdentity(
                identityUri = "https://buildsomething.fun".toUri(),
                iconUri = "favicon.ico".toUri(),
                identityName = "buildsomething.fun",
            )
        )
    }

    single {
        MobileWalletAdapterWrapper(
            activityResultSenderProvider = get(),
            walletAdapter = get(),
            blockchain = if (BuildConfig.DEBUG) Solana.Devnet else Solana.Mainnet,
        )
    }

    single<WalletRepository> {
        WalletRepositoryImpl(
            mobileWalletAdapter = get(),
            userRepository = get(),
            appPreferences = get(),
            connection = Connection(if (BuildConfig.DEBUG) RpcUrl.Devnet else RpcUrl.Mainnet)
        )
    }
}