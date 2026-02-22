package dapp.buildsomething.repository.user

import kotlinx.coroutines.flow.Flow
import dapp.buildsomething.repository.user.model.User

interface UserRepository {

    val user: Flow<User>

    suspend fun getUser(): User

    suspend fun update(block: User.() -> User)

    suspend fun logOut()
}
