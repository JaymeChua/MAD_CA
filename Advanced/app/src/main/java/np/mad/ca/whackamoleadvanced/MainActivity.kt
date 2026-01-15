package np.mad.ca.whackamoleadvanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import np.mad.ca.whackamoleadvanced.ui.theme.WhackAMoleAdvancedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhackAMoleAdvancedTheme {
                AppNavigation()
            }
        }
    }
}
@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Define navigation routes
    NavHost(navController = navController, startDestination = "game_screen") {
        composable("game_screen") {
            GameScreen(
                onNavigateToSettings = { navController.navigate("settings_screen") }
            )
        }
        composable("settings_screen") {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}