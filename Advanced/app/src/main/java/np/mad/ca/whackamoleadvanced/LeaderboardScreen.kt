package np.mad.ca.whackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(dao: GameDao, onBack: () -> Unit) {
    val scores by produceState<List<UserScoreResult>>(initialValue = emptyList()) {
        value = dao.getLeaderboard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { p ->
        LazyColumn(modifier = Modifier.padding(p).fillMaxSize().padding(16.dp)) {
            items(scores) { item ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item.username)
                    Text(item.score.toString())
                }
                HorizontalDivider()
            }
        }
    }
}