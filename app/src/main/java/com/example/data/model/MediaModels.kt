package com.example.data.model

import androidx.annotation.DrawableRes

enum class MediaType(val label: String) {
    MOVIE("Movie"),
    TV_SHOW("TV Series"),
    ANIME("Anime"),
    DOCUMENTARY("Documentary")
}

data class StreamingPlatform(
    val name: String,
    val hexColor: Long,
    val badgeText: String,
    val qualityOffered: String = "4K HDR"
)

data class CastMember(
    val name: String,
    val role: String,
    val country: String,
    val avatarUrl: String = ""
)

data class AudioTrackOption(
    val id: String,
    val language: String,
    val label: String,
    val audioFormat: String = "Dolby Atmos 5.1",
    val channelLayout: String = "5.1 Surround",
    val isOriginal: Boolean = false
)

data class SubtitleTrackOption(
    val id: String,
    val language: String,
    val label: String,
    val isOff: Boolean = false
)

data class EpisodeItem(
    val episodeNumber: Int,
    val seasonNumber: Int,
    val title: String,
    val duration: String = "48m",
    val overview: String = "",
    val thumbnail: String? = null
)

data class StreamQualityOption(
    val label: String,
    val resolution: String,
    val bitrate: String,
    val videoUrl: String
)

data class MediaItem(
    val id: String,
    val title: String,
    val originalTitle: String = "",
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    @DrawableRes val localBackdropRes: Int? = null,
    val mediaType: MediaType,
    val genres: List<String>,
    val subGenres: List<String> = emptyList(),
    val country: String,
    val countryFlag: String,
    val originalLanguage: String,
    val audioLanguages: List<String>,
    val subtitleLanguages: List<String>,
    val releaseYear: Int,
    val rating: Double,
    val ratingCount: String,
    val ageRating: String,
    val quality: String = "4K Ultra HD",
    val duration: String,
    val matchScore: Int,
    val streamingPlatforms: List<StreamingPlatform>,
    val cast: List<CastMember>,
    val director: String,
    val tagline: String = "",
    val imdbId: String = "",
    val tmdbId: Int? = null,
    val seasonNumber: Int = 1,
    val episodeNumber: Int = 1,
    val trailerYoutubeId: String = "dQw4w9WgXcQ",
    val trailerUrl: String = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    val videoStreamUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
    val audioTrackOptions: List<AudioTrackOption> = emptyList(),
    val subtitleTrackOptions: List<SubtitleTrackOption> = emptyList(),
    val qualityOptions: List<StreamQualityOption> = emptyList(),
    val isFeaturedHero: Boolean = false,
    val isTop10: Boolean = false,
    val trendingRank: Int? = null,
    val isLiveApiSource: Boolean = false,
    val isEightStreamResolved: Boolean = false
) {
    /**
     * Generates standard 1embed streaming URL as documented at https://1embed.cc/#quick-start
     * Movies: https://1embed.cc/embed/movie/{movie_id}
     * TV: https://1embed.cc/embed/tv/{show_id}/{season}/{episode}
     */
    fun get1EmbedUrl(
        season: Int = seasonNumber,
        episode: Int = episodeNumber,
        colorHex: String = "E50914",
        autoplay: Boolean = true
    ): String {
        val identifier = tmdbId?.toString() ?: (if (imdbId.isNotBlank()) imdbId else id)
        val baseUrl = if (mediaType == MediaType.TV_SHOW) {
            "https://1embed.cc/embed/tv/$identifier/$season/$episode"
        } else {
            "https://1embed.cc/embed/movie/$identifier"
        }
        val autoPlayParam = if (autoplay) "1" else "0"
        return "$baseUrl?color=$colorHex&autoplay=$autoPlayParam&autonext=1"
    }

    /**
     * Generates responsive 16:9 iframe embed HTML code according to https://1embed.cc/#quick-start
     */
    fun get1EmbedIframeCode(
        season: Int = seasonNumber,
        episode: Int = episodeNumber,
        colorHex: String = "E50914",
        autoplay: Boolean = true
    ): String {
        val url = get1EmbedUrl(season, episode, colorHex, autoplay)
        return """
            <!-- 1Embed Player - $title -->
            <div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; border-radius: 12px; background: #07080d;">
              <iframe 
                src="$url" 
                style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;" 
                frameborder="0" 
                allowfullscreen 
                allow="encrypted-media; autoplay; fullscreen">
              </iframe>
            </div>
        """.trimIndent()
    }

    fun getAvailableSeasons(): List<Int> {
        return if (mediaType == MediaType.TV_SHOW || mediaType == MediaType.ANIME) listOf(1, 2, 3) else listOf(1)
    }

    fun getEpisodesForSeason(season: Int): List<EpisodeItem> {
        val count = when (season) {
            1 -> 8
            2 -> 8
            3 -> 6
            else -> 6
        }
        val epTitles = listOf(
            "Pilot: The Awakening",
            "Threshold of Whispers",
            "Shadows in Convergence",
            "Fractured Memories",
            "Point of No Return",
            "The Hidden Frequency",
            "The Final Reckoning",
            "Horizon Reborn"
        )
        return (1..count).map { ep ->
            EpisodeItem(
                episodeNumber = ep,
                seasonNumber = season,
                title = epTitles.getOrElse(ep - 1) { "Episode $ep" },
                duration = "${45 + (ep * 3) % 15}m",
                overview = "As tensions escalate, new discoveries redefine the path forward and test allegiances.",
                thumbnail = backdropUrl.ifEmpty { posterUrl }
            )
        }
    }
}

enum class WatchlistStatus(val label: String, val badgeColor: Long) {
    PLAN_TO_WATCH("Plan to Watch", 0xFF00E5FF),
    WATCHING("Watching", 0xFFA855F7),
    COMPLETED("Completed", 0xFF00E676),
    DROPPED("On Hold", 0xFFFF3D71)
}

data class FilterState(
    val selectedCategory: String = "ALL", // ALL, MOVIE, TV_SHOW, ANIME, DOCUMENTARY
    val selectedCountry: String? = null,
    val selectedLanguage: String? = null,
    val selectedGenre: String? = null,
    val selectedSubGenre: String? = null,
    val selectedPlatform: String? = null,
    val minRating: Float = 0f,
    val releaseYearRange: ClosedFloatingPointRange<Float> = 1990f..2026f,
    val sortBy: SortOption = SortOption.POPULARITY
)

enum class SortOption(val label: String) {
    POPULARITY("Most Popular"),
    RATING_DESC("Highest IMDb"),
    MATCH_SCORE("Top Match %"),
    YEAR_DESC("Latest Release"),
    TITLE_ASC("A to Z")
}
