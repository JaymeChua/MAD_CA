package np.mad.ca.whackamoleadvanced

import androidx.room.*

@Dao
interface GameDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUser(username: String): User?

    @Insert
    suspend fun insertScore(score: Score)

    // Requirement: See personal best against other users
    @Query("""
        SELECT u.username, MAX(s.score) as score 
        FROM users u 
        INNER JOIN scores s ON u.userId = s.userId 
        GROUP BY u.userId 
        ORDER BY score DESC
    """)
    suspend fun getLeaderboard(): List<UserScoreResult>
}

data class UserScoreResult(
    val username: String,
    val score: Int
)