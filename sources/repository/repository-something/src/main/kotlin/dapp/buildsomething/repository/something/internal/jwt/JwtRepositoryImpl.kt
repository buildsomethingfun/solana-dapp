package dapp.buildsomething.repository.something.internal.jwt

import dapp.buildsomething.repository.preferences.AppPreferences
import dapp.buildsomething.repository.preferences.get
import dapp.buildsomething.repository.preferences.observe
import dapp.buildsomething.repository.preferences.set
import dapp.buildsomething.repository.something.internal.jwt.model.JwtToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class JwtRepositoryImpl(
    private val appPreferences: AppPreferences,
) : JwtRepository {

    override fun getToken(): JwtToken? {
        return appPreferences.get<AccessTokenPreference>() as? JwtToken
    }

    override fun setToken(token: JwtToken) {
        appPreferences.set<AccessTokenPreference>(token)
    }

    override fun observe(): Flow<JwtToken?> {
        return appPreferences.observe<AccessTokenPreference>()
            .map { it as? JwtToken }
    }

    override fun clear() {
        appPreferences.set<AccessTokenPreference>(null)
    }
}
