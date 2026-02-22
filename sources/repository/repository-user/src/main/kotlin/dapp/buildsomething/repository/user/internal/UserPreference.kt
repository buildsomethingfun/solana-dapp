package dapp.buildsomething.repository.user.internal

import androidx.annotation.Keep
import dapp.buildsomething.repository.preferences.Preference
import dapp.buildsomething.repository.user.model.User

@Keep
internal data object UserPreference : Preference<User> {
    override val key: String = "Key:User"
    override val defaultValue = User.Anonymous
    override val serializer = User.serializer()
}
