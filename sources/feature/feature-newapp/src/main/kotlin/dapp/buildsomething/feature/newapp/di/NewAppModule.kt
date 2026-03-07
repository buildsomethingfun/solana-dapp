package dapp.buildsomething.feature.newapp.di

import dapp.buildsomething.feature.newapp.presentation.NewAppReducer
import dapp.buildsomething.feature.newapp.presentation.NewAppStore
import dapp.buildsomething.feature.newapp.presentation.NewAppStoreProvider
import dapp.buildsomething.feature.newapp.presentation.NewAppUiStateMapper
import dapp.buildsomething.feature.newapp.presentation.actor.ChatActor
import dapp.buildsomething.feature.newapp.presentation.actor.LoadChatHistoryActor
import dapp.buildsomething.feature.newapp.presentation.model.NewAppState
import org.koin.dsl.module

val NewAppModule = module {
    factory<NewAppStoreProvider> {
        object : NewAppStoreProvider() {
            override fun provide(): NewAppStore {
                return NewAppStore(
                    initialState = NewAppState(),
                    actors = setOf(
                        ChatActor(chatInteractor = get()),
                        LoadChatHistoryActor(chatInteractor = get()),
                    ),
                    reducer = NewAppReducer(),
                    uiStateMapper = NewAppUiStateMapper(),
                )
            }
        }
    }
}
