package dapp.buildsomething.environment

class Signing(
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String
) {

    companion object {
        val debug: Signing
            get() = Signing(
                storePassword = "android",
                keyAlias = "androiddebugkey",
                keyPassword = "android"
            )

        val release: Signing
            get() = Signing(
                storePassword = "1b163597ebe8",
                keyAlias = "actions",
                keyPassword = "1b163597ebe8"
            )
    }
}
