package dapp.buildsomething.common.network.backendurl

import dapp.buildsomething.common.network.BuildConfig.BUILD_TYPE

sealed interface BackendUrl {

    val app: String

    data object Dev : BackendUrl {
//        override val app = "https://backend-kappa-nine-81.vercel.app/"
//        override val app = "https://beta.buildsomething.fun/"
        override val app = "https://backend-phi-seven-52.vercel.app/"
    }

    data object Prod : BackendUrl {
        override val app = "https://app.buildsomething.fun/"
    }

    @Suppress("KotlinConstantConditions")
    companion object {

        val Environmental: BackendUrl
            get() = when (BUILD_TYPE) {
                "release" -> Prod
                else -> Dev
            }
    }
}
