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
import com.github.guibrisson.roadmaps.ui.screen.home.HomeRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap.RoadmapRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap_folder.RoadmapFolderRoute

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
                navArgument(NavigationUtils.ROADMAP_FOLDER_ARGS) { type = NavType.StringType },
            ),
        ) {
            RoadmapFolderRoute(
                onBack = { navController.navigateUp() },
                onFolder = { navigateToFolder(navController, it) },
                onItem = {  },
            )
        }
    }
}

object NavigationUtils {
    // Args
    const val ROADMAP_ID_ARG = "roadmapId"
    const val ROADMAP_FOLDER_ARGS = "roadmapFolderArgs"

    //Screen
    const val ROADMAP_SCREEN = "roadmap"
    const val ROADMAP_FOLDER_SCREEN = "roadmapFolder"

    // Routes
    const val HOME_ROUTE = "home"
    const val ROADMAP_ROUTE = "$ROADMAP_SCREEN/{$ROADMAP_ID_ARG}"
    const val ROADMAP_FOLDER_ROUTE = "$ROADMAP_FOLDER_SCREEN/{$ROADMAP_FOLDER_ARGS}"

    // Navigation
    fun navigateToFolder(navController: NavHostController, args: Array<String>) {
        val navArgs: String = args.joinToString()
        navController.navigate(route = "$ROADMAP_FOLDER_SCREEN/$navArgs")
    }
}
