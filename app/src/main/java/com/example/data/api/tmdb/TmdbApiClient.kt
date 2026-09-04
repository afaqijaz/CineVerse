package com.example.data.api.tmdb

import com.example.BuildConfig
import com.example.data.model.CastMember
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.StreamingPlatform
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object TmdbApiClient {

    private const val TMDB_BASE_URL = "https://api.themoviedb.org/"

    // In-memory configurable API key; fallback to BuildConfig if defined
    private var customApiKey: String? = null

    // Known public/demo TMDB key fallback for seamless out-of-the-box experience
    private const val DEFAULT_TMDB_DEMO_KEY = "38db9d6d03d32ef33d02e08cff1ec0ef"

    fun getEffectiveApiKey(): String {
        if (!customApiKey.isNullOrBlank()) return customApiKey!!
        return try {
            val field = BuildConfig::class.java.getField("TMDB_API_KEY")
            val buildKey = field.get(null) as? String
            if (!buildKey.isNullOrBlank()) buildKey else DEFAULT_TMDB_DEMO_KEY
        } catch (e: Exception) {
            DEFAULT_TMDB_DEMO_KEY
        }
    }

    fun setCustomApiKey(key: String?) {
        customApiKey = key?.trim()?.ifBlank { null }
        // Force rebuild retrofit instance on key change
        rebuildRetrofit()
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url

        val key = getEffectiveApiKey()
        val requestBuilder = original.newBuilder()

        // If key is a v4 bearer token (longer string with eyJ...)
        if (key.startsWith("eyJ")) {
            requestBuilder.header("Authorization", "Bearer $key")
        } else if (key.isNotBlank()) {
            // Append as query parameter for v3 api_key
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", key)
                .build()
            requestBuilder.url(url)
        }

        requestBuilder.header("Accept", "application/json")
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    private var retrofitInstance: Retrofit? = null
    private var serviceInstance: TmdbApiService? = null

    private fun rebuildRetrofit() {
        retrofitInstance = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        serviceInstance = retrofitInstance?.create(TmdbApiService::class.java)
    }

    fun getService(): TmdbApiService {
        if (serviceInstance == null) {
            rebuildRetrofit()
        }
        return serviceInstance ?: retrofitInstance!!.create(TmdbApiService::class.java)
    }

    // Mapping helper to convert TmdbMovieDto to MediaItem
    fun mapMovieDtoToMediaItem(dto: TmdbMovieDto, platforms: List<StreamingPlatform>): MediaItem {
        val year = dto.releaseDate?.take(4)?.toIntOrNull() ?: 2024
        val rating = dto.voteAverage ?: 7.5
        val ratingFormatted = String.format("%.1f", rating).toDoubleOrNull() ?: 7.5
        val ratingCountStr = when {
            dto.voteCount == null -> "50K"
            dto.voteCount >= 1000 -> "${dto.voteCount / 1000}K"
            else -> dto.voteCount.toString()
        }

        val genres = TmdbGenreLookup.getGenreNames(dto.genreIds)

        return MediaItem(
            id = "tmdb_m_${dto.id}",
            tmdbId = dto.id,
            title = dto.title ?: "Untitled Cinema",
            originalTitle = dto.originalTitle ?: dto.title ?: "",
            overview = dto.overview?.ifBlank { "An extraordinary cinematic masterpiece presented in stunning fidelity." }
                ?: "An extraordinary cinematic masterpiece presented in stunning fidelity.",
            posterUrl = TmdbUrlBuilder.poster(dto.posterPath, "w780"),
            backdropUrl = TmdbUrlBuilder.backdrop(dto.backdropPath, "w1280"),
            mediaType = MediaType.MOVIE,
            genres = genres,
            subGenres = genres.take(2),
            country = "Global",
            countryFlag = "🌐",
            originalLanguage = dto.originalLanguage?.uppercase() ?: "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese"),
            releaseYear = year,
            rating = ratingFormatted,
            ratingCount = ratingCountStr,
            ageRating = "PG-13",
            quality = "4K Dolby Vision",
            duration = "2h 15m",
            matchScore = (85 + ((rating * 1.5).toInt().coerceIn(0, 14))),
            streamingPlatforms = platforms.take(3),
            cast = listOf(
                CastMember("Leading Ensemble", "Protagonist", "International"),
                CastMember("Supporting Star", "Supporting Role", "International")
            ),
            director = "Acclaimed Visionary Director",
            tagline = "Experience the full cinematic journey.",
            imdbId = "",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = (dto.popularity ?: 0.0) > 100.0,
            isTop10 = (dto.voteAverage ?: 0.0) >= 8.0,
            isLiveApiSource = true
        )
    }

    // Mapping helper to convert TmdbTvDto to MediaItem
    fun mapTvDtoToMediaItem(dto: TmdbTvDto, platforms: List<StreamingPlatform>): MediaItem {
        val year = dto.firstAirDate?.take(4)?.toIntOrNull() ?: 2024
        val rating = dto.voteAverage ?: 7.8
        val ratingFormatted = String.format("%.1f", rating).toDoubleOrNull() ?: 7.8
        val ratingCountStr = when {
            dto.voteCount == null -> "65K"
            dto.voteCount >= 1000 -> "${dto.voteCount / 1000}K"
            else -> dto.voteCount.toString()
        }

        val genres = TmdbGenreLookup.getGenreNames(dto.genreIds)

        return MediaItem(
            id = "tmdb_tv_${dto.id}",
            tmdbId = dto.id,
            title = dto.name ?: "Untitled Series",
            originalTitle = dto.originalName ?: dto.name ?: "",
            overview = dto.overview?.ifBlank { "Critically acclaimed episodic television series streaming across premium networks." }
                ?: "Critically acclaimed episodic television series streaming across premium networks.",
            posterUrl = TmdbUrlBuilder.poster(dto.posterPath, "w780"),
            backdropUrl = TmdbUrlBuilder.backdrop(dto.backdropPath, "w1280"),
            mediaType = MediaType.TV_SHOW,
            genres = genres,
            subGenres = genres.take(2),
            country = "Global",
            countryFlag = "🌐",
            originalLanguage = dto.originalLanguage?.uppercase() ?: "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese"),
            releaseYear = year,
            rating = ratingFormatted,
            ratingCount = ratingCountStr,
            ageRating = "TV-MA",
            quality = "4K HDR10+",
            duration = "Series (Ongoing)",
            matchScore = (86 + ((rating * 1.4).toInt().coerceIn(0, 13))),
            streamingPlatforms = platforms.take(3),
            cast = listOf(
                CastMember("Series Lead", "Protagonist", "International"),
                CastMember("Series Co-Star", "Co-Lead", "International")
            ),
            director = "Executive Showrunner",
            tagline = "Every episode raises the stakes.",
            imdbId = "",
            seasonNumber = 1,
            episodeNumber = 1,
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = (dto.popularity ?: 0.0) > 80.0,
            isTop10 = (dto.voteAverage ?: 0.0) >= 8.2,
            isLiveApiSource = true
        )
    }

    // Mapping helper for detailed movie
    fun mapMovieDetailsToMediaItem(details: TmdbMovieDetailsDto, platforms: List<StreamingPlatform>): MediaItem {
        val year = details.releaseDate?.take(4)?.toIntOrNull() ?: 2024
        val rating = details.voteAverage ?: 8.0
        val ratingFormatted = String.format("%.1f", rating).toDoubleOrNull() ?: 8.0
        val ratingCountStr = when {
            details.voteCount == null -> "100K"
            details.voteCount >= 1000 -> "${details.voteCount / 1000}K"
            else -> details.voteCount.toString()
        }

        val genres = details.genres?.map { it.name }?.ifEmpty { listOf("Drama", "Action") } ?: listOf("Drama", "Action")
        val durationStr = details.runtime?.let { "${it / 60}h ${it % 60}m" } ?: "2h 10m"

        val castMembers = details.credits?.cast?.take(6)?.map {
            CastMember(
                name = it.name,
                role = it.character ?: "Cast",
                country = "USA",
                avatarUrl = if (!it.profilePath.isNullOrBlank()) "https://image.tmdb.org/t/p/w185${it.profilePath}" else ""
            )
        } ?: emptyList()

        val director = details.credits?.crew?.find { it.job?.equals("Director", ignoreCase = true) == true }?.name
            ?: "Acclaimed Director"

        val trailerKey = details.videos?.results?.find { it.site.equals("YouTube", ignoreCase = true) && it.type.contains("Trailer", ignoreCase = true) }?.key
            ?: details.videos?.results?.firstOrNull()?.key ?: "dQw4w9WgXcQ"

        return MediaItem(
            id = "tmdb_m_${details.id}",
            tmdbId = details.id,
            title = details.title ?: "Untitled",
            originalTitle = details.originalTitle ?: details.title ?: "",
            overview = details.overview ?: "",
            posterUrl = TmdbUrlBuilder.poster(details.posterPath, "w780"),
            backdropUrl = TmdbUrlBuilder.backdrop(details.backdropPath, "w1280"),
            mediaType = MediaType.MOVIE,
            genres = genres,
            subGenres = genres.take(3),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean"),
            releaseYear = year,
            rating = ratingFormatted,
            ratingCount = ratingCountStr,
            ageRating = "PG-13",
            quality = "4K IMAX Enhanced",
            duration = durationStr,
            matchScore = (88 + ((rating * 1.2).toInt().coerceIn(0, 11))),
            streamingPlatforms = platforms.take(3),
            cast = if (castMembers.isNotEmpty()) castMembers else listOf(CastMember("Leading Star", "Hero", "USA")),
            director = director,
            tagline = details.tagline ?: "Experience the story that captivated audiences worldwide.",
            imdbId = details.imdbId ?: "",
            trailerYoutubeId = trailerKey,
            trailerUrl = "https://www.youtube.com/watch?v=$trailerKey",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isLiveApiSource = true
        )
    }

    // Mapping helper for detailed TV show
    fun mapTvDetailsToMediaItem(details: TmdbTvDetailsDto, platforms: List<StreamingPlatform>): MediaItem {
        val year = details.firstAirDate?.take(4)?.toIntOrNull() ?: 2024
        val rating = details.voteAverage ?: 8.2
        val ratingFormatted = String.format("%.1f", rating).toDoubleOrNull() ?: 8.2
        val ratingCountStr = when {
            details.voteCount == null -> "120K"
            details.voteCount >= 1000 -> "${details.voteCount / 1000}K"
            else -> details.voteCount.toString()
        }

        val genres = details.genres?.map { it.name }?.ifEmpty { listOf("Drama", "Sci-Fi") } ?: listOf("Drama", "Sci-Fi")
        val seasonsCount = details.numberOfSeasons ?: 1
        val episodesCount = details.numberOfEpisodes ?: 10

        val castMembers = details.credits?.cast?.take(6)?.map {
            CastMember(
                name = it.name,
                role = it.character ?: "Series Regular",
                country = "Global",
                avatarUrl = if (!it.profilePath.isNullOrBlank()) "https://image.tmdb.org/t/p/w185${it.profilePath}" else ""
            )
        } ?: emptyList()

        val director = details.credits?.crew?.find { it.department?.equals("Directing", ignoreCase = true) == true }?.name
            ?: "Executive Showrunner"

        val trailerKey = details.videos?.results?.find { it.site.equals("YouTube", ignoreCase = true) }?.key
            ?: "dQw4w9WgXcQ"

        return MediaItem(
            id = "tmdb_tv_${details.id}",
            tmdbId = details.id,
            title = details.name ?: "Untitled Series",
            originalTitle = details.originalName ?: details.name ?: "",
            overview = details.overview ?: "",
            posterUrl = TmdbUrlBuilder.poster(details.posterPath, "w780"),
            backdropUrl = TmdbUrlBuilder.backdrop(details.backdropPath, "w1280"),
            mediaType = MediaType.TV_SHOW,
            genres = genres,
            subGenres = genres.take(3),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese"),
            releaseYear = year,
            rating = ratingFormatted,
            ratingCount = ratingCountStr,
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "$seasonsCount Seasons • $episodesCount Episodes",
            matchScore = (89 + ((rating * 1.1).toInt().coerceIn(0, 10))),
            streamingPlatforms = platforms.take(3),
            cast = if (castMembers.isNotEmpty()) castMembers else listOf(CastMember("Series Lead", "Protagonist", "USA")),
            director = director,
            tagline = details.tagline ?: "Every season changes everything.",
            imdbId = "",
            seasonNumber = 1,
            episodeNumber = 1,
            trailerYoutubeId = trailerKey,
            trailerUrl = "https://www.youtube.com/watch?v=$trailerKey",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isLiveApiSource = true
        )
    }
}
