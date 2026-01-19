package np.mad.ca.whackamoleadvanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import np.mad.ca.whackamoleadvanced.ui.theme.WhackAMoleAdvancedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "game-db").build()
        val dao = db.gameDao()
        setContent {
            WhackAMoleAdvancedTheme {
                AppNavigationAdvanced(dao)
            }
        }
    }
}