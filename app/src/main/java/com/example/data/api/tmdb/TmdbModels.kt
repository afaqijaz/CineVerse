package com.example.data.api.tmdb

import com.example.data.model.CastMember
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.StreamingPlatform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbPagedResponse<T>(
    @Json(name = "page") val page: Int = 1,
    @Json(name = "results") val results: List<T> = emptyList(),
    @Json(name = "total_pages") val totalPages: Int = 1,
    @Json(name = "total_results") val totalResults: Int = 0
)

@JsonClass(generateAdapter = true)
data class TmdbMovieDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "original_title") val originalTitle: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null,
    @Json(name = "popularity") val popularity: Double? = null,
    @Json(name = "genre_ids") val genreIds: List<Int>? = null,
    @Json(name = "original_language") val originalLanguage: String? = null
)

@JsonClass(generateAdapter = true)
data class TmdbTvDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String? = null,
    @Json(name = "original_name") val originalName: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null,
    @Json(name = "popularity") val popularity: Double? = null,
    @Json(name = "genre_ids") val genreIds: List<Int>? = null,
    @Json(name = "original_language") val originalLanguage: String? = null
)

@JsonClass(generateAdapter = true)
data class TmdbGenreDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String = ""
)

@JsonClass(generateAdapter = true)
data class TmdbCastDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String = "",
    @Json(name = "character") val character: String? = null,
    @Json(name = "profile_path") val profilePath: String? = null
)

@JsonClass(generateAdapter = true)
data class TmdbCrewDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String = "",
    @Json(name = "job") val job: String? = null,
    @Json(name = "department") val department: String? = null
)

@JsonClass(generateAdapter = true)
data class TmdbCreditsDto(
    @Json(name = "cast") val cast: List<TmdbCastDto> = emptyList(),
    @Json(name = "crew") val crew: List<TmdbCrewDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TmdbVideoDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "key") val key: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "site") val site: String = "",
    @Json(name = "type") val type: String = ""
)

@JsonClass(generateAdapter = true)
data class TmdbVideosDto(
    @Json(name = "results") val results: List<TmdbVideoDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TmdbMovieDetailsDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "original_title") val originalTitle: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "runtime") val runtime: Int? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null,
    @Json(name = "genres") val genres: List<TmdbGenreDto>? = null,
    @Json(name = "tagline") val tagline: String? = null,
    @Json(name = "imdb_id") val imdbId: String? = null,
    @Json(name = "credits") val credits: TmdbCreditsDto? = null,
    @Json(name = "videos") val videos: TmdbVideosDto? = null
)

@JsonClass(generateAdapter = true)
data class TmdbTvDetailsDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String? = null,
    @Json(name = "original_name") val originalName: String? = null,
    @Json(name = "overview") val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    @Json(name = "number_of_seasons") val numberOfSeasons: Int? = null,
    @Json(name = "number_of_episodes") val numberOfEpisodes: Int? = null,
    @Json(name = "vote_average") val voteAverage: Double? = null,
    @Json(name = "vote_count") val voteCount: Int? = null,
    @Json(name = "genres") val genres: List<TmdbGenreDto>? = null,
    @Json(name = "tagline") val tagline: String? = null,
    @Json(name = "credits") val credits: TmdbCreditsDto? = null,
    @Json(name = "videos") val videos: TmdbVideosDto? = null
)

object TmdbGenreLookup {
    private val genres = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Sci-Fi",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western",
        10759 to "Action & Adventure",
        10762 to "Kids",
        10763 to "News",
        10764 to "Reality",
        10765 to "Sci-Fi & Fantasy",
        10766 to "Soap",
        10767 to "Talk",
        10768 to "War & Politics"
    )

    fun getGenreNames(ids: List<Int>?): List<String> {
        if (ids.isNullOrEmpty()) return listOf("Drama")
        return ids.mapNotNull { genres[it] }.ifEmpty { listOf("Featured") }
    }
}

object TmdbUrlBuilder {
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

    fun poster(path: String?, size: String = "w780"): String {
        if (path.isNullOrBlank()) return "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800&q=80"
        val cleanPath = if (path.startsWith("/")) path.substring(1) else path
        return "$IMAGE_BASE_URL$size/$cleanPath"
    }

    fun backdrop(path: String?, size: String = "w1280"): String {
        if (path.isNullOrBlank()) return "https://images.unsplash.com/photo-1574267432553-4b4628081c31?w=1200&q=80"
        val cleanPath = if (path.startsWith("/")) path.substring(1) else path
        return "$IMAGE_BASE_URL$size/$cleanPath"
    }
}
