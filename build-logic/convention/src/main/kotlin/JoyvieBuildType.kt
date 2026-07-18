/**
 * Build variants of the app. The applicationId suffix lets debug and release
 * builds be installed side by side on the same device.
 */
enum class JoyvieBuildType(
    val applicationIdSuffix: String? = null,
    val versionNameSuffix: String? = null,
) {
    DEBUG(applicationIdSuffix = ".debug", versionNameSuffix = "-debug"),
    RELEASE,
}
