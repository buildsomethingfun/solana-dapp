package dapp.buildsomething.feature.profile.presentation.model

import dapp.buildsomething.repository.something.internal.api.model.PublisherResponse

internal sealed interface ProfileEvent {

    data class ProfileLoaded(val publisher: PublisherResponse) : ProfileEvent

    data class ProfileLoadFailed(val message: String) : ProfileEvent

    data object LogOutCompleted : ProfileEvent
}

internal sealed interface ProfileUIEvent : ProfileEvent {

    data object LoadProfile : ProfileUIEvent

    data object SignOutClicked : ProfileUIEvent
}
