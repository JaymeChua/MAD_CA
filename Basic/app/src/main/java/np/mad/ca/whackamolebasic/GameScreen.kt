package np.mad.ca.whackamolebasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigateToSettings: () -> Unit) {
    // Basic Game State
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var moleIndex by remember { mutableStateOf(-1) }
    var isGameRunning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Whack-a-Mole") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score and Time Display
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Score: $score", style = MaterialTheme.typography.headlineSmall)
                Text(text = "Time: $timeLeft", style = MaterialTheme.typography.headlineSmall)
            }

            // 3x3 Grid of Buttons
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.size(300.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(9) { index ->
                    Button(
                        modifier = Modifier.height(80.dp),
                        onClick = {
                            if (isGameRunning && index == moleIndex) {
                                score++
                                moleIndex = -1 // Mole disappears when hit
                            }
                        }
                    ) {
                        if (index == moleIndex) {
                            Text("Mole")
                        } else {
                            Text("Hole")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Start/Restart Button
            Button(onClick = {
                score = 0
                timeLeft = 30
                isGameRunning = true
            }) {
                Text(if (isGameRunning) "Restart" else "Start Game")
            }
        }
    }
}