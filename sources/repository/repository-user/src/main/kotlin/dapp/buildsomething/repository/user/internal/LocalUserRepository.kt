package dapp.buildsomething.repository.user.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import dapp.buildsomething.common.util.startWith
import dapp.buildsomething.repository.preferences.AppPreferences
import dapp.buildsomething.repository.preferences.get
import dapp.buildsomething.repository.preferences.observe
import dapp.buildsomething.repository.preferences.set
import dapp.buildsomething.repository.user.UserRepository
import dapp.buildsomething.repository.user.model.User

internal class LocalUserRepository(
    private val appPreferences: AppPreferences,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : UserRepository {

    override val user = MutableSharedFlow<User>(replay = 1)

    init {
        appPreferences.observe<UserPreference>()
            .filterNotNull()
            .filterIsInstance<User>()
            .distinctUntilChanged()
            .startWith(User.Anonymous)
            .onEach(user::emit)
            .launchIn(coroutineScope)
    }

    override suspend fun update(block: User.() -> User) {
        val snapshot = getUser()

        withContext(Dispatchers.Main) {
            appPreferences.set<UserPreference>(snapshot.block())
        }
    }

    override suspend fun getUser(): User {
        return withContext(Dispatchers.IO) {
            appPreferences.get<UserPreference>() as? User
                ?: User.Anonymous
        }
    }

    override suspend fun logOut() {
        withContext(Dispatchers.Main) {
            appPreferences.set<UserPreference>(User.Anonymous)
        }
    }
}
