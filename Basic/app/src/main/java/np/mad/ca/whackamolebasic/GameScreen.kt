package np.mad.ca.whackamolebasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigateToSettings: () -> Unit) {
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var moleIndex by remember { mutableIntStateOf(-1) }
    var isGameRunning by remember { mutableStateOf(false) }

    // Game Logic Timers
    LaunchedEffect(isGameRunning) {
        if (isGameRunning) {
            while (timeLeft > 0) {
                kotlinx.coroutines.delay(1000L)
                timeLeft--
            }
            isGameRunning = false
        }
    }

    LaunchedEffect(isGameRunning) {
        if (isGameRunning) {
            while (timeLeft > 0) {
                kotlinx.coroutines.delay(800L)
                moleIndex = (0..8).random()
            }
            moleIndex = -1
        }
    }

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
            Spacer(modifier = Modifier.height(16.dp))

            // Score and Time Display
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Score", style = MaterialTheme.typography.labelLarge)
                    Text(text = "$score", style = MaterialTheme.typography.headlineMedium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Time Left", style = MaterialTheme.typography.labelLarge)
                    Text(text = "${timeLeft}s", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3x3 Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.size(320.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(9) { index ->
                    val isMole = index == moleIndex

                    // Button appearance changes based on mole presence
                    Button(
                        modifier = Modifier.aspectRatio(1f),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMole) Color(0xFF8D6E63) else Color(0xFFE0E0E0),
                            contentColor = if (isMole) Color.White else Color.Transparent
                        ),
                        onClick = {
                            if (isGameRunning && isMole) {
                                score++
                                moleIndex = -1
                            }
                        }
                    ) {
                        if (isMole) {
                            // Mole Icon
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Mole",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Control Button
            Button(
                onClick = {
                    if (!isGameRunning) {
                        score = 0
                        timeLeft = 30
                        isGameRunning = true
                    } else {
                        isGameRunning = false
                        moleIndex = -1
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)
            ) {
                Text(if (isGameRunning) "Stop Game" else "Start Game")
            }

            // Game Over Notification
            if (timeLeft == 0 && !isGameRunning) {
                Text(
                    text = "Final Score: $score",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}