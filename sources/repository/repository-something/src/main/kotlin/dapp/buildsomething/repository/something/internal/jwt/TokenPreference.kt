package dapp.buildsomething.repository.something.internal.jwt

import androidx.annotation.Keep
import dapp.buildsomething.repository.preferences.Preference
import dapp.buildsomething.repository.something.internal.jwt.model.JwtToken

@Keep
internal data object AccessTokenPreference : Preference<JwtToken> {
    override val key: String = "Key:AccessToken"
    override val defaultValue: JwtToken? = null
}
