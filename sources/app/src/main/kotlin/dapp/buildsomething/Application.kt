package dapp.buildsomething

import android.app.Application
import dapp.buildsomething.common.network.di.CommonNetworkModule
import dapp.buildsomething.di.AppModule
import dapp.buildsomething.feature.apps.di.AppsModule
import dapp.buildsomething.feature.auth.di.AuthModule
import dapp.buildsomething.feature.splash.di.SplashModule
import dapp.buildsomething.repository.preferences.di.AppPreferencesModule
import dapp.buildsomething.repository.solana.di.SolanaModule
import dapp.buildsomething.repository.something.di.SomethingModule
import dapp.buildsomething.repository.user.di.UserRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Application)
            modules(
                AppModule,
                AppPreferencesModule,
                CommonNetworkModule,
                SolanaModule,
                SomethingModule,
                UserRepositoryModule,
                SplashModule,
                AuthModule,
                AppsModule,
            )
        }
    }
}