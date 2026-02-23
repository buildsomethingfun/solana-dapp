package dapp.buildsomething.repository.something.internal.jwt

import dapp.buildsomething.repository.something.internal.jwt.model.JwtToken
import kotlinx.coroutines.flow.Flow

internal interface JwtRepository {

    fun getToken(): JwtToken?

    fun setToken(token: JwtToken)

    fun observe(): Flow<JwtToken?>

    fun clear()
}
