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

    // region Home
    val HomeDestination get() = "home?section={section}"
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
