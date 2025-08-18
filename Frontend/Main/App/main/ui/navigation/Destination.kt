package ui.navigation

import ProjectContext

object Route {
    // region Init
    lateinit var projectContext: ProjectContext
    val webHostUrl = projectContext.webHostUrl
    val deepLinkHostUrl = projectContext.deepLinkHostUrl

    fun set(projectContext: ProjectContext) {
        this.projectContext = projectContext
    }
    // endregion

    // region login
    val LoginDestination get() = "login"
    // endregion

    // region poe
    val PoetDestination get() = "poet"
    // endregion

    // region otp
    val OtpVerificationDestination get() = "otp-verification?purpose={purpose}&data={data}"

    enum class OtpPurpose {
        LOGIN_VERIFICATION
    }

    fun OtpVerificationDestinationBuilder(
        purpose: OtpPurpose,
        data: Any
    ): String {
        return OtpVerificationDestination.replace("{purpose}", purpose.name)
            .replace("{data}", data.toString())
    }
    // endregion

    // region Home
    val HomeDestination get() = "home?section={section}"

    enum class HomeSection {
        COLORS,
        MEMORIES,
        SEARCH,
        ACCOUNT
    }

    fun HomeDestinationBuilder(
        section: HomeSection = HomeSection.COLORS
    ): String {
        return HomeDestination.replace("{section}", section.name)
    }
    // endregion

    // region About
    val AboutDestination get() = "about"
    val AboutDeepLink get() = "${webHostUrl}about"
    val AboutCustomDeepLink get() = "${deepLinkHostUrl}about"

    // endregion

    // region Security
    val SecuritySettingDestination get() = "security/setting"
    val SecuritySettingDeepLink get() = "${webHostUrl}security/setting"
    val SecuritySettingCustomDeepLink get() = "${deepLinkHostUrl}security/setting"

    // endregion

    // region Help
    val HelpAndSupportDestination get() = "help"
    val HelpAndSupportDeepLink get() = "${webHostUrl}help"
    val HelpAndSupportCustomDeepLink get() = "${deepLinkHostUrl}help"

    // endregion

    // region Account
    val AccountSettingDestination get() = "account/setting"
    val AccountSettingDeepLink get() = "${webHostUrl}account/setting"
    val AccountSettingCustomDeepLink get() = "${deepLinkHostUrl}account/setting"

    // endregion

    // region Notification
    val NotificationSettingDestination get() = "notification/setting"
    val NotificationSettingDeepLink get() = "${webHostUrl}notification/setting"
    val NotificationSettingCustomDeepLink get() = "${deepLinkHostUrl}notification/setting"

    // endregion
}
