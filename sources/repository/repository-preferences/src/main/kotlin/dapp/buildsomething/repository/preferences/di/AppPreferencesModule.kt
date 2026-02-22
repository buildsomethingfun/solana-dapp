package dapp.buildsomething.repository.preferences.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import dapp.buildsomething.repository.preferences.AppPreferences
import dapp.buildsomething.repository.preferences.dataStore
import dapp.buildsomething.repository.preferences.internal.AppPreferencesImpl

val AppPreferencesModule = module {
    single<AppPreferences> {
        AppPreferencesImpl(
            dataStore = androidContext().dataStore,
            json = get(qualifier = StringQualifier("AppJson")),
        )
    }
}
