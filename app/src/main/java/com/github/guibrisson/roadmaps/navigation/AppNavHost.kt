package com.github.guibrisson.roadmaps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.guibrisson.roadmaps.ui.screen.home.HomeRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap.RoadmapRoute
import com.github.guibrisson.roadmaps.ui.screen.roadmap_folder.RoadmapFolderRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoutes.HOME_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationRoutes.HOME_ROUTE) {
            HomeRoute(
                onRoadmap = { roadmapId ->
                    navController.navigate("${NavigationRoutes.ROADMAP_SCREEN}/$roadmapId")
                },
            )
        }

        composable(
            route = NavigationRoutes.ROADMAP_ROUTE,
            arguments = listOf(
                navArgument(NavigationRoutes.ROADMAP_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            RoadmapRoute(
                onBack = { navController.navigateUp() },
                onFolder = { folderId ->
                    navController.navigate("${NavigationRoutes.ROADMAP_FOLDER_SCREEN}/$folderId")
                }
            )
        }

        composable(
            route = NavigationRoutes.ROADMAP_FOLDER_ROUTE,
            arguments = listOf(
                navArgument(NavigationRoutes.ROADMAP_FOLDER_ID_ARG) { type = NavType.StringType },
            ),
        ) {
            RoadmapFolderRoute(
                onBack = { navController.navigateUp() },
            )
        }
    }
}

object NavigationRoutes {
    // Args
    const val ROADMAP_ID_ARG = "roadmapId"
    const val ROADMAP_FOLDER_ID_ARG = "roadmapFolderId"

    //Screen
    const val ROADMAP_SCREEN = "roadmap"
    const val ROADMAP_FOLDER_SCREEN = "roadmapFolder"

    // Routes
    const val HOME_ROUTE = "home"
    const val ROADMAP_ROUTE = "$ROADMAP_SCREEN/{$ROADMAP_ID_ARG}"
    const val ROADMAP_FOLDER_ROUTE = "$ROADMAP_FOLDER_SCREEN/{$ROADMAP_FOLDER_ID_ARG}"
}
