package ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import font.resources.Res
import font.resources.ic_add
import font.resources.ic_search
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.BurgerMenu
import ui.component.Drawer
import utils.pxToDp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val viewModel = viewModel { HomeScreenViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerRect = remember { mutableStateOf(IntRect.Zero) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.title,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    BurgerMenu {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.ToggleOpenDrawer)
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            when (state.mainTab) {
                MainTab.DASHBOARD -> {
                    ExtendedFloatingActionButton(
                        text = {
                            Text("Search")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_search),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                }

                MainTab.THINGS -> {
                    ExtendedFloatingActionButton(
                        text = {
                            Text("Add")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_add),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                }

                MainTab.FLEETS -> {
                    ExtendedFloatingActionButton(
                        text = {
                            Text("Add")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_add),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                }

                MainTab.LOGS -> {}
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = state.isShowDrawer,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it }
            ) {
                Drawer(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(vertical = 8.dp)
                        .padding(start = 8.dp)
                        .width(240.dp)
                        .onGloballyPositioned {
                            drawerRect.value = it.boundsInRoot().roundToIntRect()
                        },
                    onGoToDashboard = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToDashboard)
                        }
                    },
                    onGoToThings = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToThings)
                        }
                    },
                    onGoToFleets = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToFleets)
                        }
                    },
                    onGoToLogs = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToLogs)
                        }
                    }
                )
            }

            when (state.mainTab) {
                MainTab.DASHBOARD -> DashboardPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.THINGS -> ThingsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.FLEETS -> FleetsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.LOGS -> LogsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun DashboardPane(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
            .fillMaxSize()
    ) {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Map"
                )
            }
        }
    }
}

@Composable
fun ThingsPane(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
    ) {
        Text("ThingsPane")
    }
}

@Composable
fun FleetsPane(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
    ) {
        Text("FleetsPane")
    }
}

@Composable
fun LogsPane(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
    ) {
        Text("LogsPane")
    }
}
