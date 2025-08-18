package ui.navigation

import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainNavigation(
    controller: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {

    NavHost(
        modifier = modifier,
        navController = controller,
        startDestination = Route.HomeDestination,
        enterTransition = { slideIn { IntOffset(x = it.width, y = 0) } },
        popEnterTransition = { slideIn { IntOffset(x = -it.width, y = 0) } },
        exitTransition = { slideOut { IntOffset(x = -it.width, y = 0) } },
        popExitTransition = { slideOut { IntOffset(x = it.width, y = 0) } },
    ) {
        composable(
            route = Route.LoginDestination
        ) {

        }

        composable(
            route = Route.PoetDestination
        ) {

        }

        composable(
            route = Route.OtpVerificationDestination,
            arguments = listOf(
                navArgument("purpose") {
                    defaultValue = null
                    nullable = true
                }
            ),
        ) {

        }

        composable(
            route = Route.HomeDestination,
            arguments = listOf(
                navArgument("section") {
                    defaultValue = Route.HomeSection.COLORS.name
                    nullable = true
                }
            ),
        ) {

        }

        composable(
            route = Route.AboutDestination,
            deepLinks = listOf(
                navDeepLink { uriPattern = Route.AboutDeepLink },
                navDeepLink { uriPattern = Route.AboutCustomDeepLink }
            )
        ) {

        }

        composable(
            route = Route.SecuritySettingDestination,
            deepLinks = listOf(
                navDeepLink { uriPattern = Route.SecuritySettingDeepLink },
                navDeepLink { uriPattern = Route.SecuritySettingCustomDeepLink }
            )
        ) {

        }

        composable(
            route = Route.HelpAndSupportDestination,
            deepLinks = listOf(
                navDeepLink { uriPattern = Route.HelpAndSupportDeepLink },
                navDeepLink { uriPattern = Route.HelpAndSupportCustomDeepLink }
            )
        ) {

        }

        composable(
            route = Route.AccountSettingDestination,
            deepLinks = listOf(
                navDeepLink { uriPattern = Route.AccountSettingDeepLink },
                navDeepLink { uriPattern = Route.AccountSettingCustomDeepLink }
            )
        ) {

        }

        composable(
            route = Route.NotificationSettingDestination,
            deepLinks = listOf(
                navDeepLink { uriPattern = Route.NotificationSettingDeepLink },
                navDeepLink { uriPattern = Route.NotificationSettingCustomDeepLink }
            )
        ) {

        }
    }
}
