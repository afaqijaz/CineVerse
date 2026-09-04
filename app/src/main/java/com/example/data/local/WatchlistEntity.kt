package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_items")
data class WatchlistEntity(
    @PrimaryKey val id: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val mediaType: String,
    val rating: Double,
    val releaseYear: Int,
    val country: String,
    val countryFlag: String,
    val duration: String,
    val genresString: String,
    val addedTimestamp: Long = System.currentTimeMillis(),
    val status: String = "PLAN_TO_WATCH", // PLAN_TO_WATCH, WATCHING, COMPLETED, DROPPED
    val isDownloaded: Boolean = false,
    val downloadSizeMb: Int = 1450,
    val userPersonalRating: Float = 0f,
    val userNotes: String = ""
)
