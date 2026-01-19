package np.mad.ca.whackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenAdvanced(
    user: User,
    dao: GameDao,
    onGoLeaderboard: () -> Unit
) {
    // Game State
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var moleIndex by remember { mutableIntStateOf(-1) }
    var isRunning by remember { mutableStateOf(false) }

    // UI State for Personal Best
    var personalBest by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    /**
     * Load the user's personal best from the database on screen load.
     */
    LaunchedEffect(user.userId) {
        val scores = dao.getLeaderboard()
        personalBest = scores.find { it.username == user.username }?.score ?: 0
    }

    /**
     * Game Timer Logic:
     * Decrements time every 1s and saves the score to Room when finished.
     */
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
            // Requirement: Save score to database on game end
            scope.launch {
                dao.insertScore(Score(userId = user.userId, score = score))
                // Refresh local personal best display
                val updatedScores = dao.getLeaderboard()
                personalBest = updatedScores.find { it.username == user.username }?.score ?: score
            }
        }
    }

    /**
     * Mole Movement Logic:
     * Moves the mole to a random hole every 800ms.
     */
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(800L)
                moleIndex = (0..8).random()
            }
            moleIndex = -1
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player: ${user.username}") },
                actions = {
                    IconButton(onClick = onGoLeaderboard) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Leaderboard")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Professional Stats Header
            Text(
                text = "Your High Score: $personalBest",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Current Score", style = MaterialTheme.typography.labelSmall)
                    Text(text = "$score", style = MaterialTheme.typography.headlineMedium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Time Left", style = MaterialTheme.typography.labelSmall)
                    Text(text = "${timeLeft}s", style = MaterialTheme.typography.headlineMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3x3 Grid with proper spacing and shape
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .size(320.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(9) { index ->
                    val isMole = index == moleIndex
                    Button(
                        onClick = {
                            if (isRunning && isMole) {
                                score++
                                moleIndex = -1 // Mole is whacked
                            }
                        },
                        modifier = Modifier.aspectRatio(1f),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMole) Color(0xFF8D6E63) else Color(0xFFE0E0E0),
                            contentColor = if (isMole) Color.White else Color.Transparent
                        )
                    ) {
                        if (isMole) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Mole Icon",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Primary Action Button
            Button(
                onClick = {
                    if (!isRunning) {
                        score = 0
                        timeLeft = 30
                        isRunning = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                enabled = !isRunning
            ) {
                Text(if (isRunning) "Whack them!" else "Start New Game")
            }

            // Game Over Text
            if (timeLeft == 0 && !isRunning) {
                Text(
                    text = "Game Over! Final: $score",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}