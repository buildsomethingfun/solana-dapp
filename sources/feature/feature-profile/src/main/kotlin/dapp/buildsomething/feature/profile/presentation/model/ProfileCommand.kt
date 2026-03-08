package dapp.buildsomething.feature.profile.presentation.model

internal sealed interface ProfileCommand {

    data object LoadProfile : ProfileCommand

    data object SignOut : ProfileCommand
}
