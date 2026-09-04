package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_items ORDER BY addedTimestamp DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_items WHERE status = :status ORDER BY addedTimestamp DESC")
    fun getWatchlistByStatus(status: String): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_items WHERE isDownloaded = 1 ORDER BY addedTimestamp DESC")
    fun getDownloadedItems(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_items WHERE id = :id LIMIT 1")
    fun getWatchlistById(id: String): Flow<WatchlistEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_items WHERE id = :id)")
    fun isInWatchlist(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: WatchlistEntity)

    @Query("DELETE FROM watchlist_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE watchlist_items SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE watchlist_items SET isDownloaded = :isDownloaded WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean)

    @Query("UPDATE watchlist_items SET userPersonalRating = :rating, userNotes = :notes WHERE id = :id")
    suspend fun updateUserFeedback(id: String, rating: Float, notes: String)
}
