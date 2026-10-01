package com.example.adamandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.adamandroid.ui.theme.AdamAndroidTheme
import com.example.adamandroid.ui.theme.scanmusic.ScanMusicViewModel
import com.example.adamandroid.ui.theme.screens.HomeScreen
import com.example.adamandroid.ui.theme.screens.LoginScreen
import com.example.adamandroid.ui.theme.screens.FavoritesScreen
import com.example.adamandroid.ui.theme.screens.PlayerScreen
import com.example.adamandroid.ui.theme.screens.PlaylistScreen
import com.example.adamandroid.ui.theme.screens.ScanMusicScreen
import com.example.adamandroid.viewmodel.MusicPlayerViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AdamAndroidTheme {
                val viewModel: MusicPlayerViewModel = viewModel()
                AppNavigation(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: MusicPlayerViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        // Không cho quay lại Login bằng nút Back
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onFavoritesClick = {
                    navController.navigate("favorites") {
                        // cho quay lại Login bằng nút Back
                        popUpTo("home") {
                            inclusive = false
                        }
                    }
                },

                onPlaylistClick = {
                    navController.navigate("playlist") {
                        // cho quay lại Login bằng nút Back
                        popUpTo("home") {
                            inclusive = false
                        }
                    }
                },

                onSongClick = { songId ->

                    navController.navigate(
                        "player/$songId"
                    )
                },

                onScanSongClick = {
                    navController.navigate("scan_music") {
                        // cho quay lại Login bằng nút Back
                        popUpTo("home") {
                            inclusive = false
                        }
                    }
                }
            )
        }


        // Favorites
        composable("favorites") {
            FavoritesScreen(
                viewModel = viewModel,
                onSongClick = { songId ->

                    navController.navigate(
                        "player/$songId"
                    )
                }
            )
        }

        // Playlist
        composable("playlist") {
            PlaylistScreen(
                viewModel = viewModel,
                onSongClick = { songId ->

                    navController.navigate(
                        "player/$songId"
                    )
                }
            )
        }

        //player
        composable(
            route = "player/{songId}",
            arguments = listOf(
                navArgument("songId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val songId =
                backStackEntry.arguments?.getInt("songId") ?: return@composable

            PlayerScreen(
                songId = songId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("scan_music") {

            val scanViewModel: ScanMusicViewModel = viewModel()

//            ScanMusicScreen(
//                viewModel = scanViewModel,
//
//                onAddSongs = { scannedSongs ->
//
//                    viewModel.addScannedSongs(scannedSongs)
//
//                    navController.popBackStack()
//                },
//
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )

            ScanMusicScreen(
                viewModel = scanViewModel,

                onAddSongs = { scannedSongs ->
                    viewModel.addScannedSongs(scannedSongs)

                    navController.popBackStack()
                },

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
