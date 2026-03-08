package dapp.buildsomething.feature.profile.presentation.model

internal data class ProfileState(
    val isLoading: Boolean = true,
    val displayName: String = "",
    val email: String = "",
    val walletAddress: String = "",
    val avatarUrl: String? = null,
)
