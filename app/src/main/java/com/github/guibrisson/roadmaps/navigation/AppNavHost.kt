package com.github.guibrisson.roadmaps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.guibrisson.roadmaps.navigation.NavigationUtils.navigateToFolder
import com.github.guibrisson.roadmaps.navigation.NavigationUtils.navigateToItem
import com.github.guibrisson.roadmaps.ui.screen.home.HomeRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap.RoadmapRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap_folder.RoadmapFolderRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap_item.RoadmapItemRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationUtils.HOME_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationUtils.HOME_ROUTE) {
            HomeRoute(
                onRoadmap = { roadmapId ->
                    navController.navigate("${NavigationUtils.ROADMAP_SCREEN}/$roadmapId")
                },
            )
        }

        composable(
            route = NavigationUtils.ROADMAP_ROUTE,
            arguments = listOf(
                navArgument(NavigationUtils.ROADMAP_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            RoadmapRoute(
                onBack = { navController.navigateUp() },
                onFolder = { navigateToFolder(navController, it) }
            )
        }

        composable(
            route = NavigationUtils.ROADMAP_FOLDER_ROUTE,
            arguments = listOf(
                navArgument(NavigationUtils.ROADMAP_TOPIC_ARGS) { type = NavType.StringType },
            ),
        ) {
            RoadmapFolderRoute(
                onBack = { navController.navigateUp() },
                onFolder = { navigateToFolder(navController, it) },
                onItem = { navigateToItem(navController, it) },
            )
        }

        composable(
            route = NavigationUtils.ROADMAP_ITEM_ROUTE,
            arguments = listOf(
                navArgument(NavigationUtils.ROADMAP_TOPIC_ARGS) { type = NavType.StringType },
            ),
        ) {
            RoadmapItemRoute(
                onBack = { navController.navigateUp() }
            )
        }
    }
}

object NavigationUtils {
    // Args
    const val ROADMAP_ID_ARG = "roadmapId"
    const val ROADMAP_TOPIC_ARGS = "roadmapTopicArgs"

    //Screen
    const val ROADMAP_SCREEN = "roadmap"
    private const val ROADMAP_FOLDER_SCREEN = "roadmapFolder"
    private const val ROADMAP_ITEM_SCREEN = "roadmapItem"

    // Routes
    const val HOME_ROUTE = "home"
    const val ROADMAP_ROUTE = "$ROADMAP_SCREEN/{$ROADMAP_ID_ARG}"
    const val ROADMAP_FOLDER_ROUTE = "$ROADMAP_FOLDER_SCREEN/{$ROADMAP_TOPIC_ARGS}"
    const val ROADMAP_ITEM_ROUTE = "$ROADMAP_ITEM_SCREEN/{$ROADMAP_TOPIC_ARGS}"

    fun navigateToFolder(navController: NavHostController, args: Array<String>) {
        val navArgs: String = args.joinToString()
        navController.navigate(route = "$ROADMAP_FOLDER_SCREEN/$navArgs")
    }

    fun navigateToItem(navController: NavHostController, args: Array<String>) {
        val navArgs: String = args.joinToString()
        navController.navigate(route = "$ROADMAP_ITEM_SCREEN/$navArgs")
    }
}
