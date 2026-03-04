package dapp.buildsomething.repository.onboarding.internal

import androidx.annotation.Keep
import dapp.buildsomething.repository.preferences.Preference

@Keep
internal data object OnboardedPreference : Preference<Boolean> {
    override val key: String = "Key:IsOnboarded"
    override val defaultValue: Boolean = false
}
