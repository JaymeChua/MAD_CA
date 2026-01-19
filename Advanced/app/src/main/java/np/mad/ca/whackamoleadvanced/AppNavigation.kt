package np.mad.ca.whackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun AppNavigationAdvanced(dao: GameDao) {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<User?>(null) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(dao) { user ->
                currentUser = user
                navController.navigate("game")
            }
        }
        composable("game") {
            currentUser?.let { user ->
                GameScreenAdvanced(user, dao) { navController.navigate("leaderboard") }
            }
        }
        composable("leaderboard") {
            LeaderboardScreen(dao) { navController.popBackStack() }
        }
    }
}

@Composable
fun LoginScreen(dao: GameDao, onLoginSuccess: (User) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Wack-a-Mole Advanced", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch {
                    val user = dao.getUser(username)
                    if (user != null && user.passwordHash == password) onLoginSuccess(user)
                    else msg = "Error: Invalid login"
                }
            }) { Text("Sign In") }
            Button(onClick = {
                scope.launch {
                    if (dao.getUser(username) == null) {
                        dao.insertUser(User(username = username, passwordHash = password))
                        msg = "User Registered!"
                    } else msg = "User already exists"
                }
            }) { Text("Sign Up") }
        }
        Text(msg, color = MaterialTheme.colorScheme.error)
    }
}